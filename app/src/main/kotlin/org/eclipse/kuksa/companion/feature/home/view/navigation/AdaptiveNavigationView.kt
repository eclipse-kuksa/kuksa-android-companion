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

package org.eclipse.kuksa.companion.feature.home.view.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.extension.windowSizeClass

/**
 * AdaptiveNavigationView will add depending on the [WindowWidthSizeClass] of the device a horizontal or vertical
 * NavigationBar. If the device has a [WindowWidthSizeClass] of [WindowWidthSizeClass.Compact] it will use a horizontal
 * NavigationBar, while otherwise a Vertical NavigationRail is used.
 */
@Composable
fun AdaptiveNavigationView(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    onPageSelected: (NavigationPage) -> Unit = {},
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        HorizontalNavigationView(modifier, onPageSelected)
    } else {
        VerticalNavigationView(modifier, onPageSelected)
    }
}

@Composable
@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
private fun AdaptiveNavigationViewPreview() {
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    AdaptiveNavigationView(windowSizeClass = windowSizeClass)
}
