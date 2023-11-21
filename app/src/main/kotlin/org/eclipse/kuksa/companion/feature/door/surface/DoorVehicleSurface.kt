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

import android.app.Application
import android.util.Log
import android.view.SurfaceHolder
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.ramses.AndroidRamsesSurface

class DoorVehicleSurface : AndroidRamsesSurface<DoorVehicleScene, DoorControlViewModel> {
    private lateinit var doorVehicleSceneThread: DoorVehicleSceneThread

    override fun loadScene(viewModel: DoorControlViewModel): DoorVehicleScene {
        val context = viewModel.getApplication<Application>()
        doorVehicleSceneThread = DoorVehicleSceneThread("VehicleSceneThread", viewModel)
        doorVehicleSceneThread.initRamsesThreadAndLoadScene(
            context.assets,
            "G05.ramses",
            "G05.rlogic",
        )
        doorVehicleSceneThread.moveCamera(
            yawValue = 90F,
            pitchValue = 100F,
            camDistanceValue = 700F,
        )

        return doorVehicleSceneThread
    }

    fun startRendering() {
        doorVehicleSceneThread.addRunnableToThreadQueue {
            val isDisplayCreated = doorVehicleSceneThread.isDisplayCreated
            if (!isDisplayCreated || doorVehicleSceneThread.isRendering) {
                return@addRunnableToThreadQueue
            }

            doorVehicleSceneThread.startRendering()
        }
    }

    fun stopRendering() {
        doorVehicleSceneThread.addRunnableToThreadQueue {
            if (!doorVehicleSceneThread.isRendering) {
                return@addRunnableToThreadQueue
            }

            doorVehicleSceneThread.stopRendering()
        }
    }

    // region: SurfaceHolder
    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated: surfaceHolder = $holder")

        val surface = holder.surface
        try {
            doorVehicleSceneThread.createDisplayAndShowScene(surface, null)
            doorVehicleSceneThread.renderingFramerate = MAX_FRAME_RATE
            startRendering()
        } catch (e: InterruptedException) {
            Log.e(TAG, "surfaceCreated failed", e)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surfaceChanged: holder = $holder, format = $format, width = $width, height = $height")

        doorVehicleSceneThread.resizeDisplay(width, height)
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed() called with: surfaceHolder = $surfaceHolder")

        try {
            if (doorVehicleSceneThread.isAlive) {
                doorVehicleSceneThread.destroyDisplay()
            }
        } catch (e: InterruptedException) {
            Log.e(TAG, "surfaceDestroyed failed", e)
        }
    }
    // endregion

    companion object {
        private const val MAX_FRAME_RATE = 30F
    }
}
