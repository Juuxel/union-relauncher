plugins {
    java
    `maven-publish`
}

group = "io.github.juuxel"
version = "0.0.0"

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    compileOnly("cpw.mods:securejarhandler:2.1.24")
}

tasks.withType<JavaCompile> {
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

publishing {
    publications.register<MavenPublication>("maven") {
        from(components["java"])
    }
}
