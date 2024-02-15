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

package org.eclipse.kuksa.companion.feature.sheet.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
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
 * AdaptiveSheetView adds a lash, which can be expanded on click to contain additional content. While it will be placed
 * on the bottom for devices with a [WindowWidthSizeClass] of [WindowWidthSizeClass.Compact] it will be placed on the
 * "end" for devices with a WindowWidthSizeClass which is higher.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveSheetView(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    isSheetEnabled: Boolean = true,
    sheetContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { },
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        val initialSheetValue = if (isSheetEnabled) SheetValue.PartiallyExpanded else SheetValue.Hidden

        BottomSheetView(
            modifier = modifier,
            initialSheetValue = initialSheetValue,
            sheetContent = sheetContent,
            content = content,
        )
    } else {
        SideSheetView(
            modifier = modifier,
            isSideSheetEnabled = isSheetEnabled,
            sheetContent = sheetContent,
            content = content,
        )
    }
}

@Composable
@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
private fun AdaptiveSheetViewPreview() {
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    AdaptiveSheetView(windowSizeClass) {
    }
}
