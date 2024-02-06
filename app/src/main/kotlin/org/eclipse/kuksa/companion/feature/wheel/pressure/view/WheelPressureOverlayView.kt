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

package org.eclipse.kuksa.companion.feature.wheel.pressure.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.extension.alignDriverBackDoor
import org.eclipse.kuksa.companion.extension.alignDriverFrontDoor
import org.eclipse.kuksa.companion.extension.alignPassengerBackDoor
import org.eclipse.kuksa.companion.extension.alignPassengerFrontDoor
import org.eclipse.kuksa.companion.extension.windowSizeClass
import org.eclipse.kuksa.companion.feature.wheel.pressure.viewmodel.WheelPressureViewModel

@Composable
fun WheelPressureOverlayView(
    viewModel: WheelPressureViewModel,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    WheelPressureOverlayView(
        viewModel.pressureLeftFront,
        viewModel.pressureRightFront,
        viewModel.pressureLeftBack,
        viewModel.pressureRightBack,
        windowSizeClass,
        modifier,
    )
}

@Composable
private fun WheelPressureOverlayView(
    pressureLeftFront: Int,
    pressureRightFront: Int,
    pressureLeftBack: Int,
    pressureRightBack: Int,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val (driverSideRef, passengerSideRef, driverSideBackRef, passengerSideBackRef) = createRefs()

        val anchorPoint = createRef()
        Spacer(
            Modifier
                .size(2.dp)
                .constrainAs(anchorPoint) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
        )

        val unit = "kPa"
        Text(
            text = "$pressureLeftFront $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.White,
            modifier = Modifier
                .constrainAs(driverSideRef) {
                    alignDriverFrontDoor(windowSizeClass, anchorPoint)
                },
        )
        Text(
            text = "$pressureRightFront $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.White,
            modifier = Modifier
                .constrainAs(passengerSideRef) {
                    alignPassengerFrontDoor(windowSizeClass, anchorPoint)
                },
        )
        Text(
            text = "$pressureLeftBack $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.White,
            modifier = Modifier
                .constrainAs(driverSideBackRef) {
                    alignDriverBackDoor(windowSizeClass, anchorPoint)
                },
        )
        Text(
            text = "$pressureRightBack $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.White,
            modifier = Modifier
                .constrainAs(passengerSideBackRef) {
                    alignPassengerBackDoor(windowSizeClass, anchorPoint)
                },
        )
    }
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
@Composable
private fun WheelPressureControlViewPreview() {
    val viewModel = WheelPressureViewModel()
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    Surface {
        WheelPressureOverlayView(viewModel, windowSizeClass)
    }
}
