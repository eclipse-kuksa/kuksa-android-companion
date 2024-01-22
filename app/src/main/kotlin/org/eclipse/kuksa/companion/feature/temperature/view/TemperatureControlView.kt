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

package org.eclipse.kuksa.companion.feature.temperature.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.eclipse.kuksa.companion.feature.temperature.viewmodel.TemperatureViewModel

private val DefaultElementPadding = 10.dp
private val DefaultEdgePadding = 25.dp

@Composable
fun TemperatureControlView(
    viewModel: TemperatureViewModel,
    modifier: Modifier = Modifier,
) {
    var plannedTemperature by remember {
        val temperature = viewModel.hvac.station.row1.driver.temperature
        mutableFloatStateOf(temperature.value.toFloat())
    }

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
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

@Preview
@Composable
private fun TemperatureControlViewPreview() {
    val viewModel = TemperatureViewModel()
    Surface {
        TemperatureControlView(viewModel)
    }
}
