/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * A main class wrapper for Minecraft Forge that bundles multiple file paths to one mod's
 * contents into one URL.
 *
 * <p>It uses the original {@value juuxel.unionrelauncher.UnionRelauncher#MOD_CLASSES_ENV}
 * environment variable present in Forge for Minecraft ≤1.19.2 with the same format to support build tools
 * without major changes.
 *
 * <h2>Usage</h2>
 * <ol>
 * <li>Set the {@value juuxel.unionrelauncher.UnionRelauncher#MAIN_CLASS_PROPERTY} system property to point to the original main class.</li>
 * <li>Set up the {@value juuxel.unionrelauncher.UnionRelauncher#MOD_CLASSES_ENV} environment variable with an id-path list.
 *    <ul>
 *        <li>The environment variable is a list of entries separated by {@link java.io.File#pathSeparator}.</li>
 *        <li>Each entry is one of:
 *            <ul>
 *                <li>a file path belonging to the <em>default mod</em></li>
 *                <li>an id-path pair {@code id%%path}, where {@code id} is a unique id for the mod path group
 *                and {@code path} is the file path belonging to that mod</li>
 *            </ul>
 *        </li>
 *    </ul>
 * </li>
 * <li>Launch the Union Relauncher module or jar. The main class is {@link juuxel.unionrelauncher.UnionRelauncher}.</li>
 * </ol>
 */
module io.github.juuxel.unionrelauncher {
    requires cpw.mods.securejarhandler;
    exports juuxel.unionrelauncher;
}
