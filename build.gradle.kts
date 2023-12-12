plugins {
    java
    `maven-publish`
    signing
}

group = "io.github.juuxel"
version = "1.0.0"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    compileOnly("net.minecraftforge:securemodules:2.2.7")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

val mainClass = "juuxel.unionrelauncher.UnionRelauncher"

tasks.compileJava {
    inputs.property("version", version)
    options.javaModuleMainClass.set(mainClass)
    options.javaModuleVersion.set(version.toString())
}

tasks.jar {
    manifest {
        attributes("Main-Class" to mainClass)
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

publishing {
    publications.register<MavenPublication>("maven") {
        from(components["java"])

        pom {
            name.set("Union Relauncher")
            description.set("A main class wrapper for Minecraft Forge to combine mod paths")
            url.set("https://github.com/Juuxel/union-relauncher")

            licenses {
                license {
                    name.set("Mozilla Public License Version 2.0")
                    url.set("https://www.mozilla.org/en-US/MPL/2.0/")
                }
            }

            developers {
                developer {
                    id.set("Juuxel")
                    name.set("Juuxel")
                    email.set("juuzsmods@gmail.com")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/Juuxel/union-relauncher.git")
                developerConnection.set("scm:git:ssh://github.com:Juuxel/union-relauncher.git")
                url.set("https://github.com/Juuxel/union-relauncher")
            }
        }
    }
}

if (project.hasProperty("signing.keyId")) {
    signing {
        sign(publishing.publications)
    }
}
