import java.util.Properties

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.google.devtools.ksp") version "1.9.0-1.0.11"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        flatDir {
            dirs(rootDir.absolutePath + "/libs")
        }
        mavenLocal()
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/eclipse-kuksa/kuksa-android-sdk")
            credentials {
                val localProperties = loadLocalProperties()

                username = System.getenv("GPR_USERNAME") ?: localProperties.getProperty("gpr.user")
                password = System.getenv("GPR_TOKEN") ?: localProperties.getProperty("gpr.key")
            }
        }
    }
}

include(":app")

fun loadLocalProperties(): Properties {
    return Properties().apply {
        val localProperties = file("$rootDir/local.properties")
        load(localProperties.reader())
    }
}
