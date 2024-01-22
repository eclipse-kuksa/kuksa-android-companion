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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.eclipse.kuksa.companion.CompanionApplication
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.extension.alignDriverBackDoor
import org.eclipse.kuksa.companion.extension.alignDriverFrontDoor
import org.eclipse.kuksa.companion.extension.alignPassengerBackDoor
import org.eclipse.kuksa.companion.extension.alignPassengerFrontDoor
import org.eclipse.kuksa.companion.extension.alignTrunk
import org.eclipse.kuksa.companion.extension.getWindowSizeClass
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel

val horizontalMarginAnchorToDoor = 75.dp
val verticalMarginAnchorToDoor = 10.dp
val verticalMarginAnchorToBackDoor = 100.dp

@Composable
fun DoorOverlayView(viewModel: DoorControlViewModel, windowSizeClass: WindowSizeClass, modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (driverSide, passengerSide, driverSideBack, passengerSideBack, trunkRear) = createRefs()

        val isLockedDriverSide = viewModel.door.row1.driverSide.isLocked
        val isLockedPassengerSide = viewModel.door.row1.passengerSide.isLocked
        val isLockedDriverSideBack = viewModel.door.row2.driverSide.isLocked
        val isLockedPassengerSideBack = viewModel.door.row2.passengerSide.isLocked
        val isLockedTrunkRear = viewModel.trunk.rear.isLocked

        val anchorPoint = createRef()
        Spacer(
            Modifier
                .size(2.dp)
                .background(Color.White)
                .constrainAs(anchorPoint) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
        )

        val imageModifier = Modifier
            .size(60.dp)
        Box(
            modifier = imageModifier
                .constrainAs(driverSide) {
                    alignDriverFrontDoor(windowSizeClass, anchorPoint)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedDriverSide)
                },
        ) {
            Image(
                painter = painterResource(id = viewModel.fetchLockDrawable(isLockedDriverSide.value)),
                contentDescription = "Lock top left",
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier = imageModifier
                .constrainAs(passengerSide) {
                    alignPassengerFrontDoor(windowSizeClass, anchorPoint)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedPassengerSide)
                },
        ) {
            Image(
                painter = painterResource(id = viewModel.fetchLockDrawable(isLockedPassengerSide.value)),
                contentDescription = "Lock top right",
                modifier = Modifier.fillMaxSize(),
            )
        }
        Image(
            painter = painterResource(id = viewModel.fetchLockDrawable(isLockedDriverSideBack.value)),
            contentDescription = "Lock bottom left",
            modifier = imageModifier
                .constrainAs(driverSideBack) {
                    alignDriverBackDoor(windowSizeClass, anchorPoint)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedDriverSideBack)
                },
        )
        Box(
            modifier = imageModifier
                .constrainAs(passengerSideBack) {
                    alignPassengerBackDoor(windowSizeClass, anchorPoint)
                }
                .clickable {
                    viewModel.onClickToggleDoor(isLockedPassengerSideBack)
                },
        ) {
            Image(
                painter = painterResource(id = viewModel.fetchLockDrawable(isLockedPassengerSideBack.value)),
                contentDescription = "Lock bottom right",
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier = imageModifier
                .constrainAs(trunkRear) {
                    alignTrunk(windowSizeClass, anchorPoint)
                }
                .clickable {
                    viewModel.onClickToggleTrunk(isLockedTrunkRear)
                },
        ) {
            Image(
                painter = painterResource(id = viewModel.fetchLockDrawable(isLockedTrunkRear.value)),
                contentDescription = "Lock bottom center",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
@Composable
private fun DoorOverlayPreview() {
    val viewModel = DoorControlViewModel(Application())
    val windowSizeClass = LocalConfiguration.current.getWindowSizeClass()
    Surface {
        DoorOverlayView(viewModel, windowSizeClass)
    }
}
