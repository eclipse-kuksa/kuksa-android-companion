/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

import java.util.Properties

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.google.devtools.ksp") version "1.9.20-1.0.14"
        id("de.fayard.refreshVersions") version "0.60.3"
    }
}

plugins {
    id("de.fayard.refreshVersions")
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

                username = System.getenv("GPR_USERNAME") ?: localProperties?.getProperty("gpr.user")
                password = System.getenv("GPR_TOKEN") ?: localProperties?.getProperty("gpr.key")
            }
        }
    }
}

include(":app")

fun loadLocalProperties(): Properties? {
    val localProperties = file("$rootDir/local.properties")
    if (!localProperties.exists()) return null

    localProperties.reader().use { reader ->
        return Properties().apply {
            load(reader)
        }
    }
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel.isLessStableThan(current.stabilityLevel)
    }
}
