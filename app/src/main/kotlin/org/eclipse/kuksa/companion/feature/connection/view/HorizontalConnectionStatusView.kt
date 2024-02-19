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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel.ConnectionState

val StatusBarHeight = 50.dp
private val iconSize = 30.dp
private val progressBarHeight = 25.dp
private val paddingEnd = 5.dp

@Composable
fun HorizontalConnectionStatusView(
    viewModel: ConnectionStatusViewModel,
    modifier: Modifier = Modifier,
) {
    val connectionState = viewModel.connectionState
    val connectionStateLabel = connectionState.toString()
    val backgroundColor = MaterialTheme.colorScheme.errorContainer

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(StatusBarHeight)
            .background(backgroundColor)
            .clickable(enabled = connectionState == ConnectionState.DISCONNECTED) {
                viewModel.onClickReconnect()
            },
    ) {
        Row {
            when (connectionState) {
                ConnectionState.DISCONNECTED -> {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_refresh_24),
                        contentDescription = "Reconnect",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .size(iconSize)
                            .padding(end = paddingEnd),
                    )
                }

                ConnectionState.CONNECTING -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .width(iconSize)
                            .height(progressBarHeight) // otherwise takes to much height => circle is not centered
                            .padding(end = paddingEnd),
                    )
                }

                else -> {
                    // unused
                }
            }

            Text(
                connectionStateLabel,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )
        }
    }
}

@Preview
@Composable
private fun HorizontalDisconnectedPreview() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionState.DISCONNECTED

    HorizontalConnectionStatusView(viewModel = viewModel)
}

@Preview
@Composable
private fun HorizontalConnectingPreview() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionState.CONNECTING
    HorizontalConnectionStatusView(viewModel = viewModel)
}

@Preview
@Composable
private fun HorizontalConnectedPreview() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionState.CONNECTED
    HorizontalConnectionStatusView(viewModel = viewModel)
}
