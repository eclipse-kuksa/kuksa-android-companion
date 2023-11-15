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

package org.eclipse.kuksa.demo.feature.wheel.pressure.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.eclipse.kuksa.demo.feature.wheel.pressure.viewmodel.WheelPressureViewModel

@Composable
fun WheelPressureControlView(viewModel: WheelPressureViewModel) {
    WheelPressureOverlay(viewModel = viewModel)
}

@Composable
private fun WheelPressureOverlay(viewModel: WheelPressureViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 35.dp, end = 35.dp, top = 225.dp, bottom = 90.dp),
    ) {
        val (driverSideRef, passengerSideRef, driverSideBackRef, passengerSideBackRef) = createRefs()
        val modifier = Modifier
        val doorsPaddingBottom = 50.dp

        val unit = "kPa"
        val pressureLeftFront = viewModel.pressureLeftFront
        val pressureRightFront = viewModel.pressureRightFront
        val pressureLeftBack = viewModel.pressureLeftBack
        val pressureRightBack = viewModel.pressureRightBack
        Text(
            text = "$pressureLeftFront $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.Black,
            modifier = modifier
                .constrainAs(driverSideRef) {
                    start.linkTo(parent.start)
                },
        )
        Text(
            text = "$pressureRightFront $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.Black,
            modifier = modifier
                .constrainAs(passengerSideRef) {
                    end.linkTo(parent.end)
                },
        )
        Text(
            text = "$pressureLeftBack $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.Black,
            modifier = modifier
                .constrainAs(driverSideBackRef) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, doorsPaddingBottom)
                },
        )
        Text(
            text = "$pressureRightBack $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color.Black,
            modifier = modifier
                .constrainAs(passengerSideBackRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, doorsPaddingBottom)
                },
        )
    }
}

@Preview
@Composable
private fun WheelPressureControlViewPreview() {
    Surface {
        WheelPressureControlView(WheelPressureViewModel())
    }
}
