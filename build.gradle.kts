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

plugins {
    base
    detekt
    id("com.github.ben-manes.versions") version "0.49.0"
}

subprojects {
    apply {
        plugin("ktlint")
    }
    afterEvaluate {
        tasks.check {
            finalizedBy("ktlintCheck")
        }
    }

    // see: https://kotest.io/docs/framework/tags.html#gradle
    tasks.withType<Test> {
        val systemPropertiesMap = HashMap<String, Any>()
        System.getProperties().forEach { key, value ->
            systemPropertiesMap[key.toString()] = value.toString()
        }
        systemProperties = systemPropertiesMap
    }
}
