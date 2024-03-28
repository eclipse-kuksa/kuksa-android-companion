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
package org.eclipse.kuksa.companion.feature.door.surface

import android.util.Log
import com.bmwgroup.ramses.Property
import com.bmwgroup.ramses.RamsesThread
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.ramses.AndroidRamsesScene
import org.eclipse.kuksa.vss.VssDoor
import org.eclipse.kuksa.vss.VssTrunk
import org.eclipse.kuksa.vsscore.model.VssSignal
import org.eclipse.kuksa.vsscore.model.findSignal

interface DoorVehicleScene : AndroidRamsesScene {
    fun updateDoors(door: VssDoor)

    fun updateTrunk(trunk: VssTrunk)
}

// taken from: https://github.com/bmwcarit/ramses-sample-app/blob/master/app/src/main/java/com/bmwgroup/ramsessample/fragments/VehicleSceneThread.kt
// Changes:
// - package was adapted
// - class was re-formatted using detekt formatter
// - moveCamera method was added
// - add openDoors / closeDoors / isDoorOpen and setDoor
// - add properties mDoorXValue to allow a more granular door control
// - remove touch-related events: onTouchUp, onMotion
// - Couple to ViewModels and implement DoorVehicleScene interface
// - Rename variable names
// - Uses generated VssNodes for the models
class DoorVehicleSceneThread(
    threadName: String,
    private val viewModel: DoorControlViewModel,
) : RamsesThread(threadName, viewModel.getApplication()), DoorVehicleScene {
    private var doorRow1DriverSide: VssSignal<Boolean> = VssDoor.VssRow1.VssDriverSide.VssIsOpen()
    private var doorRow1PassengerSide: VssSignal<Boolean> = VssDoor.VssRow1.VssPassengerSide.VssIsOpen()
    private var doorRow2DriverSide: VssSignal<Boolean> = VssDoor.VssRow2.VssDriverSide.VssIsOpen()
    private var doorRow2PassengerSide: VssSignal<Boolean> = VssDoor.VssRow2.VssPassengerSide.VssIsOpen()
    private var doorTrunkRear: VssSignal<Boolean> = VssTrunk.VssRear.VssIsOpen()

    private var yawValue = 0f
    private var pitchValue = 0f
    private var camDistValue = DEFAULT_CAMERA_DISTANCE
    private var screenWidth = 1
    private var screenHeight = 1

    private var cameraViewportWProperty: Property? = null
    private var cameraViewportHProperty: Property? = null
    private var cameraYawProperty: Property? = null
    private var cameraPitchProperty: Property? = null
    private var cameraDistanceProperty: Property? = null

    private var doorRow1DriverSideProperty: Property? = null
    private var doorRow1PassengerSideProperty: Property? = null
    private var doorRow2DriverSideProperty: Property? = null
    private var doorRow2PassengerSideProperty: Property? = null
    private var doorTrunkRearProperty: Property? = null

    private val Boolean.toFloat: Float
        get() = if (this) DOOR_OPEN else DOOR_CLOSED

    /*
     * Overrides the base class method which calls this based on thread scheduling
     * This method is executed from the correct thread (the one which talks to ramses)
     */
    override fun onUpdate() {
        /*
         * Set the camera yaw and pitch values based on user input (also see input callbacks below)
         */
        cameraYawProperty?.set(yawValue)
        cameraPitchProperty?.set(pitchValue)
        cameraDistanceProperty?.set(camDistValue)

        /*
         * Open or close the doors based on bool value (also see input callbacks below)
         */
        doorRow1DriverSideProperty?.set(doorRow1DriverSide.value.toFloat)
        doorRow2DriverSideProperty?.set(doorRow1PassengerSide.value.toFloat)
        doorRow1PassengerSideProperty?.set(doorRow2DriverSide.value.toFloat)
        doorRow2PassengerSideProperty?.set(doorRow2PassengerSide.value.toFloat)
        doorTrunkRearProperty?.set(doorTrunkRear.value.toFloat)
    }

    fun moveCamera(
        yawValue: Float,
        pitchValue: Float,
        camDistanceValue: Float,
    ) {
        addRunnableToThreadQueue {
            this.yawValue = yawValue
            this.pitchValue = pitchValue
            camDistValue = camDistanceValue
        }
    }

    /*
     * Overrides the base class method which calls this based on thread scheduling
     * This method is executed from the correct thread (the one which talks to ramses)
     */
    override fun onSceneLoaded() {
        // / Find the script used to control the logic state (light animation and camera control)
        val doorsScriptRootInput: Property? = getLogicNodeRootInput("SceneControls")
        val cameraScriptRootInput: Property? = getLogicNodeRootInput("CameraCrane.Interface_CameraCrane")
        if (cameraScriptRootInput == null || doorsScriptRootInput == null) {
            Log.e(TAG, "Loaded scene does not contain expected interface scripts!")
            return
        }

        cameraViewportWProperty = cameraScriptRootInput.getChild("Viewport").getChild("Width")
        cameraViewportHProperty = cameraScriptRootInput.getChild("Viewport").getChild("Height")
        cameraYawProperty = cameraScriptRootInput.getChild("CraneGimbal").getChild("Yaw")
        cameraPitchProperty = cameraScriptRootInput.getChild("CraneGimbal").getChild("Pitch")
        cameraDistanceProperty = cameraScriptRootInput.getChild("CraneGimbal").getChild("Distance")

        val yawOutput = getLogicNodeRootOutput("SceneControls")
            .getChild("CameraPerspective")
            .getChild("Yaw")
        val pitchOutput = getLogicNodeRootOutput("SceneControls")
            .getChild("CameraPerspective")
            .getChild("Pitch")
        val distanceOutput = getLogicNodeRootOutput(
            "SceneControls",
        ).getChild("CameraPerspective").getChild("Distance")
        unlinkProperties(yawOutput, cameraYawProperty)
        unlinkProperties(pitchOutput, cameraPitchProperty)
        unlinkProperties(distanceOutput, cameraDistanceProperty)

        doorRow1DriverSideProperty = doorsScriptRootInput.getChild("Door_F_L_OpeningValue")
        doorRow1PassengerSideProperty = doorsScriptRootInput.getChild("Door_B_L_OpeningValue")
        doorRow2DriverSideProperty = doorsScriptRootInput.getChild("Door_F_R_OpeningValue")
        doorRow2PassengerSideProperty = doorsScriptRootInput.getChild("Door_B_R_OpeningValue")
        doorTrunkRearProperty = doorsScriptRootInput.getChild("Tailgate_OpeningValue")

        // Initialize values from the scene defaults; in real apps the values should come from the application logic
        yawValue = cameraYawProperty?.float ?: 0f
        pitchValue = cameraPitchProperty?.float ?: 0f
    }

    /*
     * Overrides the base class method which calls this based on thread scheduling
     * This method is executed from the correct thread (the one which talks to ramses)
     */
    override fun onSceneLoadFailed() {
        // Implement actions to react to failed scene load
        Log.e(TAG, "Loading Scene failed")
    }

    override fun onLogicUpdated() {
        // Here it's possible to read out (but not write!) scene state data, like LogicNode outputs
        // (see RamsesThread::getLogicNodeRootOutput()).
    }

    /*
     * Overrides the base class method which calls this based on thread scheduling
     * This method is executed from the correct thread (the one which talks to ramses)
     */
    override fun onDisplayResize(
        width: Int,
        height: Int,
    ) {
        cameraViewportWProperty?.set(width)
        cameraViewportHProperty?.set(height)
        screenWidth = width
        screenHeight = height
    }

    // region: VehicleScene
    @Suppress("UNCHECKED_CAST") // TODO: Remove cast when .findSignal is fixed inside the SDK
    override fun updateDoors(door: VssDoor) {
        addRunnableToThreadQueue {
            door.findSignal(VssDoor.VssRow1.VssPassengerSide.VssIsOpen::class).apply {
                doorRow1DriverSide = getOrDefault(doorRow1DriverSide.vssPath, doorRow1DriverSide)
                    as VssSignal<Boolean>
                doorRow1PassengerSide = getOrDefault(doorRow1PassengerSide.vssPath, doorRow1PassengerSide)
                    as VssSignal<Boolean>
                doorRow2DriverSide = getOrDefault(doorRow2DriverSide.vssPath, doorRow2DriverSide)
                    as VssSignal<Boolean>
                doorRow2PassengerSide = getOrDefault(doorRow2PassengerSide.vssPath, doorRow2PassengerSide)
                    as VssSignal<Boolean>
            }
        }
    }

    @Suppress("UNCHECKED_CAST") // TODO: Remove cast when .findSignal is fixed inside the SDK
    override fun updateTrunk(trunk: VssTrunk) {
        addRunnableToThreadQueue {
            doorTrunkRear = trunk.findSignal(VssTrunk.VssRear.VssIsOpen()) as VssSignal<Boolean>
        }
    }
    // endregion

    private companion object {
        const val DEFAULT_CAMERA_DISTANCE = 600f

        const val DOOR_OPEN = 1.0F
        const val DOOR_CLOSED = 0.0F
    }
}
