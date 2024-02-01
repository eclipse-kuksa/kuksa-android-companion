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

package org.eclipse.kuksa.companion.feature.home.view.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.SHEET_COLLAPSED_HEIGHT
import org.eclipse.kuksa.companion.SHEET_EXPANDED_HEIGHT
import org.eclipse.kuksa.companion.extension.getWindowSizeClass

/**
 * AdaptiveSheetView adds a lash, which can be expanded on click to contain additional content. While it will be placed
 * on the bottom for devices with a [WindowWidthSizeClass] of [WindowWidthSizeClass.Compact] it will be placed on the
 * "end" for devices with a WindowWidthSizeClass which is higher.
 */
@Composable
fun AdaptiveSheetView(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val sheetRef = createRef()

        val sheetModifier = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                Modifier
                    .height(if (isExpanded) SHEET_EXPANDED_HEIGHT.dp else SHEET_COLLAPSED_HEIGHT.dp)
                    .fillMaxWidth()
                    .zIndex(2F)
                    .clickable { isExpanded = !isExpanded }
                    .background(Color.White)
                    .constrainAs(sheetRef) {
                        bottom.linkTo(parent.bottom)
                    }
            }

            else -> {
                Modifier
                    .fillMaxHeight()
                    .width(if (isExpanded) SHEET_EXPANDED_HEIGHT.dp else SHEET_COLLAPSED_HEIGHT.dp)
                    .zIndex(2F)
                    .clickable { isExpanded = !isExpanded }
                    .background(Color.White)
                    .constrainAs(sheetRef) {
                        end.linkTo(parent.end)
                    }
            }
        }

        Box(
            modifier = sheetModifier,
        ) {
            if (isExpanded) {
                sheetContent()
            } else {
                AdaptiveLine(modifier = Modifier.align(Alignment.Center), windowSizeClass = windowSizeClass)
            }
        }
    }
}

@Composable
@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
private fun AdaptiveSheetViewPreview() {
    val windowSizeClass = LocalConfiguration.current.getWindowSizeClass()
    AdaptiveSheetView(windowSizeClass) {
    }
}
