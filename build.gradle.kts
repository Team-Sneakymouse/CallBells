plugins {
    id("java")
}

repositories {
    mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
	compileOnly("me.clip:placeholderapi:2.11.5")
	implementation(fileTree("libs") {
        include("*.jar")
    })
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configure<JavaPluginConvention> {
    sourceSets {
        main {
            resources.srcDir(file("src/resources"))
        }
    }
}