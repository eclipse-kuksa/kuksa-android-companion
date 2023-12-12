import org.eclipse.kuksa.companion.extension.lib

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
    id("org.jlleitschuh.gradle.ktlint")
}

// https://github.com/jlleitschuh/ktlint-gradle#configuration
ktlint {
    version.set("1.0.1")
    android.set(true)
    debug.set(false)
    verbose.set(true)
    ignoreFailures.set(false)
    coloredOutput.set(false)
    enableExperimentalRules.set(false)

    filter {
        exclude(
            "**/generated/**",
            "**/build/**",
            "**/node_modules/**",
        )
    }
}

dependencies {
    ktlintRuleset(lib("ktlint-compose-rules"))
}
