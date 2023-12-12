/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package juuxel.unionrelauncher;

import cpw.mods.jarhandling.SecureJar;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class. See module-level documentation for more details.
 */
public final class UnionRelauncher {
    private static final String MAIN_CLASS_PROPERTY = "unionRelauncher.mainClass";
    private static final String MOD_CLASSES_ENV = "MOD_CLASSES";

    public static void main(final String[] args) throws Throwable {
        final String mainClassName = Objects.requireNonNull(System.getProperty(MAIN_CLASS_PROPERTY), "no main class defined");
        final String modClasses = Objects.requireNonNullElse(System.getenv(MOD_CLASSES_ENV), "");

        record ModPathEntry(String mod, Path path) {
        }

        // Read mods
        final var mods = Arrays.stream(modClasses.split(File.pathSeparator))
            .map(line -> line.split("%%", 2))
            .map(parts -> {
                // See https://github.com/neoforged/FancyModLoader/blob/3447c36/loader/src/main/java/net/neoforged/fml/loading/targets/CommonLaunchHandler.java#L91-L94
                final String mod = parts.length == 1 ? "defaultmodid" : parts[0];
                final Path path = Path.of(parts[parts.length - 1]);
                return new ModPathEntry(mod, path);
            })
            .collect(Collectors.groupingBy(ModPathEntry::mod));
        final Set<URI> allModUris = mods.values()
            .stream()
            .flatMap(entries -> entries.stream().map(ModPathEntry::path))
            .map(Path::toUri)
            .collect(Collectors.toSet());

        // Filter out mods from main classpath
        final Set<URI> filteredClasspath = Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
            .map(Path::of)
            .map(Path::toUri)
            .filter(uri -> !allModUris.contains(uri))
            .collect(Collectors.toSet());

        // Create unions for mods
        final Set<URI> modUris = mods.values()
            .stream()
            .map(entries -> entries.stream().map(ModPathEntry::path).toArray(Path[]::new))
            .map(SecureJar::from)
            .map(SecureJar::getRootPath)
            .map(Path::toUri)
            .collect(Collectors.toSet());

        // Combine classpath and create class loader
        final URL[] urlClasspath = Stream.concat(filteredClasspath.stream(), modUris.stream())
            .map(uri -> {
                try {
                    return uri.toURL();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })
            .toArray(URL[]::new);
        final URLClassLoader urlClassLoader = new URLClassLoader(urlClasspath, null);

        // Launch the game (or the next wrapper class in the chain, anyway)
        Thread.currentThread().setContextClassLoader(urlClassLoader);
        final Class<?> mainClass = Class.forName(mainClassName, true, urlClassLoader);
        MethodHandles.publicLookup()
            .findStatic(mainClass, "main", MethodType.methodType(void.class, String[].class))
            .invokeExact(args);
    }
}
