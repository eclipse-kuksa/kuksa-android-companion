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

package org.eclipse.kuksa.demo.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.eclipse.kuksa.demo.feature.settings.view.SettingsView
import org.eclipse.kuksa.demo.feature.settings.viewModel.SettingsViewModel

const val SettingsView = "settings"
fun NavGraphBuilder.settingsScreen(
    settingsViewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
) {
    composable(SettingsView) {
        SettingsView(
            settingsViewModel = settingsViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    navigate(SettingsView, navOptions)
}
