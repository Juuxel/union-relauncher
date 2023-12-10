plugins {
    java
    `maven-publish`
}

group = "io.github.juuxel"
version = "0.0.0"

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    compileOnly("net.minecraftforge:securemodules:2.2.7")
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
