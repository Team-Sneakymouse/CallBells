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
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
	compileOnly("me.clip:placeholderapi:2.11.5")
	implementation(fileTree("libs") {
        include("*.jar")
    })
}

configure<JavaPluginConvention> {
    sourceSets {
        main {
            resources.srcDir(file("src/resources"))
        }
    }
}