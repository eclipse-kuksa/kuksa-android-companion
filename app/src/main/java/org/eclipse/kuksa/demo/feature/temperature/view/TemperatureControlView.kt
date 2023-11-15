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

package org.eclipse.kuksa.demo.feature.temperature.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.eclipse.kuksa.demo.feature.temperature.viewmodel.TemperatureViewModel

private val DefaultElementPadding = 10.dp
private val DefaultEdgePadding = 25.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TemperatureControlView(viewModel: TemperatureViewModel) {
    BottomSheetScaffold(
        sheetContent = { BottomSheetContent(viewModel) },
        containerColor = Color.White.copy(alpha = 0f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(paddingValues = it),
        ) {
            TemperatureOverlay(viewModel = viewModel)
        }
    }
}

@Composable
private fun BottomSheetContent(viewModel: TemperatureViewModel) {
    var plannedTemperature by remember {
        val temperature = viewModel.hvac.station.row1.driver.temperature
        mutableFloatStateOf(temperature.value.toFloat())
    }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (minTemperatureRef, temperatureSliderRef, maxTemperatureRef) = createRefs()
        val (plannedTempRef, currentTemperatureRef) = createRefs()

        Text(
            text = "Planned Temperature",
            modifier = Modifier.constrainAs(plannedTempRef) {
                top.linkTo(parent.top, DefaultElementPadding)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        Text(
            text = "${viewModel.minTemperature}°C",
            modifier = Modifier
                .constrainAs(minTemperatureRef) {
                    start.linkTo(parent.start, DefaultEdgePadding)
                    top.linkTo(temperatureSliderRef.top)
                    bottom.linkTo(temperatureSliderRef.bottom)
                },
        )

        val sliderRange = viewModel.minTemperature.toFloat().rangeTo(viewModel.maxTemperature.toFloat())
        val sliderSteps = viewModel.maxTemperature - viewModel.minTemperature - 1
        Slider(
            value = plannedTemperature,
            valueRange = sliderRange,
            steps = sliderSteps,
            onValueChange = { temperature ->
                plannedTemperature = temperature
            },
            onValueChangeFinished = {
                val plannedTemp = plannedTemperature.toInt()
                viewModel.plannedTemperature = plannedTemp
                viewModel.onPlannedTemperatureChanged(plannedTemp)
            },
            modifier = Modifier
                .constrainAs(temperatureSliderRef) {
                    top.linkTo(plannedTempRef.bottom)
                    start.linkTo(minTemperatureRef.end, DefaultElementPadding)
                    end.linkTo(maxTemperatureRef.start, DefaultElementPadding)
                    width = Dimension.fillToConstraints
                },
        )

        Text(
            text = "${viewModel.maxTemperature}°C",
            modifier = Modifier
                .constrainAs(maxTemperatureRef) {
                    end.linkTo(parent.end, DefaultEdgePadding)
                    top.linkTo(temperatureSliderRef.top)
                    bottom.linkTo(temperatureSliderRef.bottom)
                },
        )

        Text(
            text = "${plannedTemperature.toInt()}°C",
            modifier = Modifier.constrainAs(currentTemperatureRef) {
                top.linkTo(temperatureSliderRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, DefaultElementPadding)
            },
        )
    }
}

@Composable
private fun TemperatureOverlay(viewModel: TemperatureViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 60.dp, end = 60.dp, top = 300.dp, bottom = 80.dp),
    ) {
        val (driverSideRef, passengerSideRef, driverSideBackRef, passengerSideBackRef) = createRefs()
        val modifier = Modifier
        val doorsPaddingBottom = 50.dp

        val temperatureDriverSideFront = viewModel.temperatureDriverSideFront
        val temperaturePassengerSideFront = viewModel.temperaturePassengerSideFront
        val temperatureDriverSideBack = viewModel.temperatureDriverSideBack
        val temperaturePassengerSideBack = viewModel.temperaturePassengerSideBack

        val unit = "°C"
        Text(
            text = "$temperatureDriverSideFront $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = viewModel.getTemperatureColor(temperatureDriverSideFront),
            modifier = modifier
                .constrainAs(driverSideRef) {
                    start.linkTo(parent.start)
                },
        )
        Text(
            text = "$temperaturePassengerSideFront $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = viewModel.getTemperatureColor(temperaturePassengerSideFront),
            modifier = modifier
                .constrainAs(passengerSideRef) {
                    end.linkTo(parent.end)
                },
        )
        Text(
            text = "$temperatureDriverSideBack $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = viewModel.getTemperatureColor(temperatureDriverSideBack),
            modifier = modifier
                .constrainAs(driverSideBackRef) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, doorsPaddingBottom)
                },
        )
        Text(
            text = "$temperaturePassengerSideBack $unit",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = viewModel.getTemperatureColor(temperaturePassengerSideBack),
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
private fun TemperatureControlPreview() {
    Surface {
        TemperatureControlView(TemperatureViewModel())
    }
}

@Preview
@Composable
private fun BottomSheetContentPreview() {
    Surface {
        BottomSheetContent(TemperatureViewModel())
    }
}
