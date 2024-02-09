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

package org.eclipse.kuksa.companion.feature.door.viewModel

import android.app.Application
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.feature.door.surface.DoorVehicleScene
import org.eclipse.kuksa.companion.listener.FilteredVssNodeListener
import org.eclipse.kuksa.vss.VssDoor
import org.eclipse.kuksa.vss.VssTrunk
import org.eclipse.kuksa.vsscore.model.VssSignal

class DoorControlViewModel(application: Application) : AndroidViewModel(application) {
    var doorVehicleSceneDelegate: DoorVehicleScene? = null

    val vssDoorListener = object : FilteredVssNodeListener<VssDoor>() {
        override fun onNodeChanged(vssNode: VssDoor) {
            door = vssNode
            doorVehicleSceneDelegate?.updateDoors(vssNode)
        }

        override fun onPostFilterError(throwable: Throwable) {
            Log.e(TAG, "Failed to subscribe to node: $throwable")
        }
    }
    val vssTrunkListener = object : FilteredVssNodeListener<VssTrunk>() {
        override fun onNodeChanged(vssNode: VssTrunk) {
            trunk = vssNode
            doorVehicleSceneDelegate?.updateTrunk(vssNode)
        }

        override fun onPostFilterError(throwable: Throwable) {
            Log.e(TAG, "Failed to subscribe to node: $throwable")
        }
    }

    var onClickOpenAll: () -> Unit = {}

    var onClickCloseAll: () -> Unit = {}

    var onClickToggleDoor: (VssSignal<Boolean>) -> Unit = {}

    var onClickToggleTrunk: (VssSignal<Boolean>) -> Unit = {}

    var trunk by mutableStateOf(VssTrunk())
        private set

    var door: VssDoor by mutableStateOf(VssDoor())
        private set

    @DrawableRes
    fun fetchLockDrawable(isLocked: Boolean): Int {
        return if (isLocked) {
            R.drawable.baseline_lock_24
        } else {
            R.drawable.baseline_lock_open_24
        }
    }

    companion object {
        val DOOR_ALL_OPEN = VssDoor(
            row1 = VssDoor.VssRow1(
                driverSide = VssDoor.VssRow1.VssDriverSide(
                    isOpen = VssDoor.VssRow1.VssDriverSide.VssIsOpen(true),
                ),
                passengerSide = VssDoor.VssRow1.VssPassengerSide(
                    isOpen = VssDoor.VssRow1.VssPassengerSide.VssIsOpen(true),
                ),
            ),
            row2 = VssDoor.VssRow2(
                driverSide = VssDoor.VssRow2.VssDriverSide(
                    isOpen = VssDoor.VssRow2.VssDriverSide.VssIsOpen(true),
                ),
                passengerSide = VssDoor.VssRow2.VssPassengerSide(
                    isOpen = VssDoor.VssRow2.VssPassengerSide.VssIsOpen(true),
                ),
            ),
        )

        val DOOR_ALL_CLOSED = VssDoor(
            row1 = VssDoor.VssRow1(
                driverSide = VssDoor.VssRow1.VssDriverSide(
                    isOpen = VssDoor.VssRow1.VssDriverSide.VssIsOpen(false),
                ),
                passengerSide = VssDoor.VssRow1.VssPassengerSide(
                    isOpen = VssDoor.VssRow1.VssPassengerSide.VssIsOpen(false),
                ),
            ),
            row2 = VssDoor.VssRow2(
                driverSide = VssDoor.VssRow2.VssDriverSide(
                    isOpen = VssDoor.VssRow2.VssDriverSide.VssIsOpen(false),
                ),
                passengerSide = VssDoor.VssRow2.VssPassengerSide(
                    isOpen = VssDoor.VssRow2.VssPassengerSide.VssIsOpen(false),
                ),
            ),
        )

        val TRUNK_OPEN = VssTrunk(rear = VssTrunk.VssRear(isOpen = VssTrunk.VssRear.VssIsOpen(true)))
        val TRUNK_CLOSED = VssTrunk(rear = VssTrunk.VssRear(isOpen = VssTrunk.VssRear.VssIsOpen(false)))
    }
}
