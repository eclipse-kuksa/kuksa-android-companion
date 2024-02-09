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

package org.eclipse.kuksa.companion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.feature.connection.factory.DataBrokerConnectorFactory
import org.eclipse.kuksa.companion.feature.connection.repository.ConnectionInfoRepository
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionViewModel
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionViewModel.ConnectionState
import org.eclipse.kuksa.companion.feature.door.surface.DoorVehicleScene
import org.eclipse.kuksa.companion.feature.door.surface.DoorVehicleSurface
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel.Companion.DOOR_ALL_CLOSED
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel.Companion.DOOR_ALL_OPEN
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel.Companion.TRUNK_CLOSED
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel.Companion.TRUNK_OPEN
import org.eclipse.kuksa.companion.feature.home.view.AdaptiveAppScreen
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.companion.feature.navigation.viewmodel.NavigationViewModel
import org.eclipse.kuksa.companion.feature.settings.viewModel.SettingsViewModel
import org.eclipse.kuksa.companion.feature.temperature.viewmodel.TemperatureViewModel
import org.eclipse.kuksa.companion.feature.wheel.pressure.viewmodel.WheelPressureViewModel
import org.eclipse.kuksa.companion.ui.theme.KuksaCompanionTheme
import org.eclipse.kuksa.connectivity.databroker.DataBrokerConnection
import org.eclipse.kuksa.connectivity.databroker.DataBrokerException
import org.eclipse.kuksa.connectivity.databroker.request.VssNodeSubscribeRequest
import org.eclipse.kuksa.connectivity.databroker.request.VssNodeUpdateRequest
import org.eclipse.kuksa.extension.vss.not
import org.eclipse.kuksa.proto.v1.Types.Field
import org.eclipse.kuksa.vss.VssAxle
import org.eclipse.kuksa.vss.VssDoor
import org.eclipse.kuksa.vss.VssHvac
import org.eclipse.kuksa.vss.VssLights
import org.eclipse.kuksa.vss.VssStation
import org.eclipse.kuksa.vss.VssTrunk
import org.eclipse.kuksa.vsscore.annotation.VssModelGenerator
import org.eclipse.kuksa.vsscore.model.VssNode
import javax.inject.Inject

@AndroidEntryPoint
@VssModelGenerator
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var connectionInfoRepository: ConnectionInfoRepository

    private lateinit var doorVehicleScene: DoorVehicleScene
    private val doorVehicleSurface = DoorVehicleSurface()

    private val connectionViewModel: ConnectionViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels()
    private val doorControlViewModel: DoorControlViewModel by viewModels()
    private val temperatureViewModel: TemperatureViewModel by viewModels()
    private val lightControlViewModel: LightControlViewModel by viewModels()
    private val wheelPressureViewModel: WheelPressureViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val dataBrokerConnectorFactory = DataBrokerConnectorFactory()

    // storing the connection in the ViewModel keeps the Connection alive on orientation changes
    private var dataBrokerConnection: DataBrokerConnection?
        get() = connectionViewModel.dataBrokerConnection
        set(value) {
            connectionViewModel.dataBrokerConnection = value
        }

    // region: Lifecycle
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doorVehicleScene = doorVehicleSurface.loadScene(doorControlViewModel)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
            KuksaCompanionTheme {
                AdaptiveAppScreen(
                    callback = doorVehicleSurface,
                    connectionViewModel = connectionViewModel,
                    navigationViewModel = navigationViewModel,
                    doorControlViewModel = doorControlViewModel,
                    temperatureViewModel = temperatureViewModel,
                    lightControlViewModel = lightControlViewModel,
                    wheelPressureViewModel = wheelPressureViewModel,
                    settingsViewModel = settingsViewModel,
                    windowSizeClass = windowSizeClass,
                )
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        doorControlViewModel.doorVehicleSceneDelegate = object : DoorVehicleScene {
            override fun updateDoors(door: VssDoor) {
                doorVehicleScene.updateDoors(door)
            }

            override fun updateTrunk(trunk: VssTrunk) {
                doorVehicleScene.updateTrunk(trunk)
            }
        }

        connectionViewModel.onClickReconnect = {
            connectToDataBroker {
                subscribe()
            }
        }

        doorControlViewModel.onClickOpenAll = {
            updateVssNode(
                DOOR_ALL_OPEN.row1.driverSide.isOpen,
                DOOR_ALL_OPEN.row1.passengerSide.isOpen,
                DOOR_ALL_OPEN.row2.driverSide.isOpen,
                DOOR_ALL_OPEN.row2.passengerSide.isOpen,
                TRUNK_OPEN.rear.isOpen,
                fields = listOf(Field.FIELD_ACTUATOR_TARGET),
            )
        }

        doorControlViewModel.onClickCloseAll = {
            updateVssNode(
                DOOR_ALL_CLOSED.row1.driverSide.isOpen,
                DOOR_ALL_CLOSED.row1.passengerSide.isOpen,
                DOOR_ALL_CLOSED.row2.driverSide.isOpen,
                DOOR_ALL_CLOSED.row2.passengerSide.isOpen,
                TRUNK_CLOSED.rear.isOpen,
                fields = listOf(Field.FIELD_ACTUATOR_TARGET),
            )
        }

        doorControlViewModel.onClickToggleDoor = { toggledSignal ->
            updateVssNode(!toggledSignal, fields = listOf(Field.FIELD_ACTUATOR_TARGET))
        }

        doorControlViewModel.onClickToggleTrunk = { toggledSignal ->
            updateVssNode(!toggledSignal, fields = listOf(Field.FIELD_ACTUATOR_TARGET))
        }

        temperatureViewModel.onPlannedTemperatureChanged = { plannedTemperature ->
            updatePlannedTemperature(plannedTemperature)
        }

        lightControlViewModel.onClickToggleLight = { vssSignal ->
            updateVssNode(!vssSignal, fields = listOf(Field.FIELD_ACTUATOR_TARGET))
        }

        connectToDataBroker {
            subscribe()
        }
    }

    override fun onResume() {
        super.onResume()

        doorVehicleSurface.startRendering()
    }

    override fun onPause() {
        super.onPause()

        doorVehicleSurface.stopRendering()
    }

    // endregion

    private fun updatePlannedTemperature(plannedTemperature: Int) {
        lifecycleScope.launch {
            val vssNodes = listOf(
                VssStation.VssRow1.VssDriver.VssTemperature(plannedTemperature),
                VssStation.VssRow1.VssPassenger.VssTemperature(plannedTemperature),
                VssStation.VssRow2.VssDriver.VssTemperature(plannedTemperature),
                VssStation.VssRow2.VssPassenger.VssTemperature(plannedTemperature),
            )

            vssNodes.forEach { vssNode ->
                val updateRequest = VssNodeUpdateRequest(vssNode, Field.FIELD_ACTUATOR_TARGET)
                dataBrokerConnection?.update(updateRequest)
            }
        }
    }

    private fun subscribe() {
        dataBrokerConnection?.apply {
            val vssDoorSubscribeRequest = VssNodeSubscribeRequest(VssDoor())
            val vssTrunkSubscribeRequest = VssNodeSubscribeRequest(VssTrunk())
            val vssHvacSubscribeRequest = VssNodeSubscribeRequest(VssHvac())
            val vssAxleSubscribeRequest = VssNodeSubscribeRequest(VssAxle())
            val vssLightsSubscribeRequest = VssNodeSubscribeRequest(VssLights())

            subscribe(vssDoorSubscribeRequest, listener = doorControlViewModel.vssDoorListener)
            subscribe(vssTrunkSubscribeRequest, listener = doorControlViewModel.vssTrunkListener)
            subscribe(vssHvacSubscribeRequest, listener = temperatureViewModel.vssTemperatureListener)
            subscribe(vssAxleSubscribeRequest, listener = wheelPressureViewModel.vssWheelPressureListener)
            subscribe(vssLightsSubscribeRequest, listener = lightControlViewModel.vssLightListener)
        }
    }

    @Suppress("performance:SpreadOperator") // Neglectable: Field types are 1-2 elements mostly
    private fun updateVssNode(
        vararg vssNodes: VssNode,
        fields: List<Field> = listOf(Field.FIELD_VALUE),
    ) {
        lifecycleScope.launch {
            try {
                vssNodes.forEach { vssNode ->
                    val updateRequest = VssNodeUpdateRequest(vssNode, *fields.toTypedArray())
                    dataBrokerConnection?.update(updateRequest)
                }
            } catch (e: DataBrokerException) {
                Log.w(TAG, "Failed to update door: $e")
            }
        }
    }

    private fun connectToDataBroker(onConnected: () -> Unit = {}) {
        // dataBrokerConnection is already established e.g. after an orientation change
        if (connectionViewModel.connectionState == ConnectionState.CONNECTED) {
            return
        }

        lifecycleScope.launch {
            val connectionInfo = connectionInfoRepository.connectionInfoFlow.first()

            try {
                Log.d(TAG, "Connecting to DataBroker ${connectionInfo.host}:${connectionInfo.port}")

                connectionViewModel.connectionState = ConnectionState.CONNECTING
                val context = this@MainActivity
                val dataBrokerConnector = dataBrokerConnectorFactory.create(context, connectionInfo)
                dataBrokerConnection = dataBrokerConnector.connect()
                connectionViewModel.connectionState = ConnectionState.CONNECTED
                onConnected()
            } catch (e: DataBrokerException) {
                Log.w(TAG, "Connection to DataBroker failed: ", e)
                connectionViewModel.connectionState = ConnectionState.DISCONNECTED
            }
        }
    }
}
