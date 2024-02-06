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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.extension.windowSizeClass

/**
 * AdaptiveLine draws a line depending on the [WindowWidthSizeClass] of the device. If the device has a
 * [WindowWidthSizeClass] of [WindowWidthSizeClass.Compact] it will draw a horizontal line, otherwise it will draw a
 * horizontal line.
 */
@Composable
fun AdaptiveLine(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    length: Dp = 50.dp,
    thickness: Dp = 4.dp,
    color: Color = Color.LightGray,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        HorizontalLine(modifier, length, thickness, color)
    } else {
        VerticalLine(modifier, length, thickness, color)
    }
}

@Composable
fun HorizontalLine(
    modifier: Modifier = Modifier,
    length: Dp = 50.dp,
    thickness: Dp = 4.dp,
    color: Color = Color.LightGray,
) {
    Spacer(
        modifier = modifier
            .background(color)
            .width(length)
            .height(thickness),
    )
}

@Composable
fun VerticalLine(
    modifier: Modifier = Modifier,
    length: Dp = 50.dp,
    thickness: Dp = 4.dp,
    color: Color = Color.LightGray,
) {
    Spacer(
        modifier = modifier
            .background(color)
            .width(thickness)
            .height(length),
    )
}

@Composable
@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
private fun AdaptiveLinePreview() {
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    Box {
        AdaptiveLine(windowSizeClass = windowSizeClass)
    }
}

@Composable
@Preview
private fun HorizontalLinePreview() {
    HorizontalLine()
}

@Composable
@Preview
private fun VerticalLinePreview() {
    VerticalLine()
}
