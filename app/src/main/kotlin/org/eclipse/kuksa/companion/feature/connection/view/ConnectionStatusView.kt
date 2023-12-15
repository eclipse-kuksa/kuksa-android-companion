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

package org.eclipse.kuksa.companion.feature.connection.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel.ConnectionState
import kotlin.time.Duration.Companion.milliseconds

private const val MAX_NUMBER_OF_DOTS = 3

private val StatusBarHeight = 30.dp
private val DelayDuration = 500.milliseconds

@Composable
fun ConnectionStatusView(
    viewModel: ConnectionStatusViewModel,
    modifier: Modifier = Modifier,
) {
    val connectionState = viewModel.connectionState
    val connectionStateLabel = connectionState.toString().lowercase()
    val backgroundColor = viewModel.backgroundColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(StatusBarHeight)
            .background(backgroundColor)
            .clickable(enabled = connectionState == ConnectionState.DISCONNECTED) {
                viewModel.onClickReconnect()
            },
    ) {
        var text = connectionStateLabel
        if (connectionState == ConnectionState.CONNECTING) {
            val numberOfDots = remember {
                mutableIntStateOf(0)
            }
            repeat(numberOfDots.intValue) {
                text += "."
            }
            LaunchedEffect(Unit) {
                while (true) {
                    if (numberOfDots.intValue < MAX_NUMBER_OF_DOTS) {
                        numberOfDots.intValue += 1
                    } else {
                        numberOfDots.intValue = 0
                    }
                    delay(DelayDuration)
                }
            }
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )

            if (connectionState == ConnectionState.DISCONNECTED) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_refresh_24),
                    contentDescription = "Reconnect",
                    colorFilter = ColorFilter.tint(Color.White),
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Preview
@Composable
fun ConnectionStatusPreview_Disconnected() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionState.DISCONNECTED

    ConnectionStatusView(viewModel = viewModel)
}

@Preview
@Composable
fun ConnectionStatusPreview_Connecting() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionState.CONNECTING
    ConnectionStatusView(viewModel = viewModel)
}

@Preview
@Composable
fun ConnectionStatusPreview_Connected() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionState.CONNECTED
    ConnectionStatusView(viewModel = viewModel)
}
