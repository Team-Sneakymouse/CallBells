plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.10"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")
}

configure<JavaPluginConvention> {
    sourceSets {
        main {
            resources.srcDir(file("src/resources"))
        }
    }
}