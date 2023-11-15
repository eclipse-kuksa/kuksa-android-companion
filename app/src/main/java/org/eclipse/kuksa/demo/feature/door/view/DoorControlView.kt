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

package org.eclipse.kuksa.demo.feature.door.view

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.eclipse.kuksa.demo.feature.door.viewModel.DoorControlViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoorControlView(
    doorControlViewModel: DoorControlViewModel,
) {
    BottomSheetScaffold(
        sheetContent = { BottomSheetContent(doorControlViewModel) },
        containerColor = Color.White.copy(alpha = 0f),
    ) {
        LockOverlay(viewModel = doorControlViewModel)
    }
}

@Composable
private fun LockOverlay(viewModel: DoorControlViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 70.dp, end = 70.dp, top = 280.dp, bottom = 80.dp),
    ) {
        val (driverSide, passengerSide, driverSideBack, passengerSideBack, trunkRear) = createRefs()
        val imageModifier = Modifier.size(50.dp)
        val doorsPaddingBottom = 130.dp

        val isLockedDriverSide = viewModel.door.row1.driverSide.isLocked
        val isLockedPassengerSide = viewModel.door.row1.passengerSide.isLocked
        val isLockedDriverSideBack = viewModel.door.row2.driverSide.isLocked
        val isLockedPassengerSideBack = viewModel.door.row2.passengerSide.isLocked
        val isLockedTrunkRear = viewModel.trunk.rear.isLocked

        Image(
            painter = painterResource(id = viewModel.fetchLockDrawable(isLockedDriverSide.value)),
            contentDescription = "Lock top left",
            modifier = imageModifier
                .constrainAs(driverSide) {
                    start.linkTo(parent.start)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedDriverSide)
                },
        )
        Image(
            painter = painterResource(id = viewModel.fetchLockDrawable(isLockedPassengerSide.value)),
            contentDescription = "Lock top right",
            modifier = imageModifier
                .constrainAs(passengerSide) {
                    end.linkTo(parent.end)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedPassengerSide)
                },
        )
        Image(
            painter = painterResource(id = viewModel.fetchLockDrawable(isLockedDriverSideBack.value)),
            contentDescription = "Lock bottom left",
            modifier = imageModifier
                .constrainAs(driverSideBack) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, doorsPaddingBottom)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedDriverSideBack)
                },
        )
        Image(
            painter = painterResource(id = viewModel.fetchLockDrawable(isLockedPassengerSideBack.value)),
            contentDescription = "Lock bottom right",
            modifier = imageModifier
                .constrainAs(passengerSideBack) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, doorsPaddingBottom)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedPassengerSideBack)
                },
        )
        Image(
            painter = painterResource(id = viewModel.fetchLockDrawable(isLockedTrunkRear.value)),
            contentDescription = "Lock bottom center",
            modifier = imageModifier
                .constrainAs(trunkRear) {
                    start.linkTo(parent.start, 150.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 40.dp)
                }
                .clickable {
                    viewModel.onClickToggleTrunk(isLockedTrunkRear)
                },
        )
    }
}

@Composable
private fun BottomSheetContent(viewModel: DoorControlViewModel) {
    Column(modifier = Modifier.padding(20.dp)) {
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

@Preview
@Composable
private fun LockOverlayPreview() {
    Surface {
        LockOverlay(DoorControlViewModel(Application()))
    }
}

@Preview
@Composable
private fun BottomSheetContentPreview() {
    Surface {
        BottomSheetContent(DoorControlViewModel(Application()))
    }
}
