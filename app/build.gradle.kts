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

import org.eclipse.kuksa.companion.property.PropertiesLoader

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.serialization)
    kotlin("android")
}

android {
    namespace = "org.eclipse.kuksa.companion"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.eclipse.kuksa.companion"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            val localProperties = PropertiesLoader().load("$rootDir/local.properties")

            val runnerTempPath = System.getenv("RUNNER_TEMP") ?: System.getProperty("user.home")
            val runnerKeystoreFilePath = "$runnerTempPath/keystore.jks"
            val runnerKeystoreFile = File(runnerKeystoreFilePath)
            val keystoreFile = if (runnerKeystoreFile.exists()) {
                runnerKeystoreFile
            } else { // For building locally - Just add the keys to the local.properties
                println("No keystore property file found - looking for a local one")
                val propertyKeystorePath = localProperties?.getProperty("release.keystore.path") ?: return@create
                File("$runnerTempPath/$propertyKeystorePath")
            }

            storeFile = keystoreFile
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                ?: localProperties?.getProperty("release.keystore.key.alias")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                ?: localProperties?.getProperty("release.keystore.key.password")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                ?: localProperties?.getProperty("release.keystore.store.password")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
        // For store releases like F-Droid where an unsigned artifact is needed
        create("unsigned") {
            initWith(getByName("release"))
            applicationIdSuffix = ".unsigned"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    androidResources {
        noCompress += "ramses"
        noCompress += "rlogic"
    }
}

dependencies {
    ksp(libs.vss.processor) {
        isChanging = true
    }

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kuksa.sdk) {
        isChanging = true
    }
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.ramses.aar)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.tooling.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.tooling.test.manifest)
}
