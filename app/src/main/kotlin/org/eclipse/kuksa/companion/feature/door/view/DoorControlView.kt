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

package org.eclipse.kuksa.companion.feature.door.view

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.SHEET_EXPANDED_HEIGHT
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel

@Composable
fun DoorControlView(
    viewModel: DoorControlViewModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(20.dp)) {
        Text(text = "Open / Close", fontWeight = FontWeight.Bold, modifier = Modifier.align(CenterHorizontally))
        Row {
            Button(onClick = { viewModel.onClickOpenAll() }, modifier = Modifier.weight(1F)) {
                Text(text = "Open")
            }
            Button(onClick = { viewModel.onClickCloseAll() }, modifier = Modifier.weight(1F)) {
                Text(text = "Close")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Button(
                onClick = { viewModel.onClickToggleDoor(viewModel.door.row1.driverSide.isOpen) },
                modifier = Modifier.weight(2F),
            ) {
                Text(text = "Driver Side")
            }
            Button(
                onClick = {
                    viewModel.onClickToggleDoor(viewModel.door.row1.passengerSide.isOpen)
                },
                modifier = Modifier.weight(2F),
            ) {
                Text(text = "Passenger Side")
            }
        }

        Row {
            Button(
                onClick = { viewModel.onClickToggleDoor(viewModel.door.row2.driverSide.isOpen) },
                modifier = Modifier.weight(1F),
            ) {
                Text(text = "Back Driver Side")
            }
            Button(
                onClick = {
                    viewModel.onClickToggleDoor(viewModel.door.row2.passengerSide.isOpen)
                },
                modifier = Modifier.weight(1F),
            ) {
                Text(text = "Back Passenger Side")
            }
        }

        Row {
            Button(
                onClick = { viewModel.onClickToggleTrunk(viewModel.trunk.rear.isOpen) },
                modifier = Modifier.weight(1F),
            ) {
                Text(text = "Trunk Rear")
            }
        }
    }
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = SHEET_EXPANDED_HEIGHT)
@Preview(widthDp = SHEET_EXPANDED_HEIGHT, heightDp = PREVIEW_WIDTH_DP)
@Composable
private fun BottomSheetContentPreview() {
    val application = Application()
    val viewModel = DoorControlViewModel(application)

    Surface {
        DoorControlView(viewModel)
    }
}
