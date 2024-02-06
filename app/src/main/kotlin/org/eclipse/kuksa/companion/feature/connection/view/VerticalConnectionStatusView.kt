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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.extension.convertToVerticalString
import org.eclipse.kuksa.companion.extension.isVisible
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel

@Composable
fun VerticalConnectionStatusView(
    viewModel: ConnectionStatusViewModel,
    modifier: Modifier = Modifier,
) {
    val connectionState = viewModel.connectionState
    val connectionStateLabel = connectionState.toString().lowercase()
    val backgroundColor = viewModel.backgroundColor

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(StatusBarHeight)
            .background(backgroundColor)
            .clickable(enabled = connectionState == ConnectionStatusViewModel.ConnectionState.DISCONNECTED) {
                viewModel.onClickReconnect()
            },
    ) {
        val isAnimating = connectionState == ConnectionStatusViewModel.ConnectionState.CONNECTING
        var text = connectionStateLabel
        text = animateLoadingText(isAnimating, text)

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (textRef, imageRef) = createRefs()

            Text(
                text = text.convertToVerticalString(),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(textRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        top.linkTo(parent.top)
                        bottom.linkTo(imageRef.top)
                    },
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_refresh_24),
                contentDescription = "Reconnect",
                colorFilter = ColorFilter.tint(Color.White),
                alignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        bottom.linkTo(parent.bottom)
                    }
                    .isVisible(connectionState == ConnectionStatusViewModel.ConnectionState.DISCONNECTED),
            )
        }
    }
}




@Preview(heightDp = 300)
@Composable
private fun VerticalDisconnectedPreview() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionStatusViewModel.ConnectionState.DISCONNECTED

    VerticalConnectionStatusView(viewModel = viewModel)
}

@Preview(heightDp = 300)
@Composable
private fun VerticalConnectingPreview() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionStatusViewModel.ConnectionState.CONNECTING
    VerticalConnectionStatusView(viewModel = viewModel)
}

@Preview(heightDp = 300)
@Composable
private fun VerticalConnectedPreview() {
    val viewModel = ConnectionStatusViewModel()
    viewModel.connectionState = ConnectionStatusViewModel.ConnectionState.CONNECTED
    VerticalConnectionStatusView(viewModel = viewModel)
}
