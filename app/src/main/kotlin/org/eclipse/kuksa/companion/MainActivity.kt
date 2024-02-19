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
import org.eclipse.kuksa.DataBrokerConnection
import org.eclipse.kuksa.DataBrokerException
import org.eclipse.kuksa.DisconnectListener
import org.eclipse.kuksa.companion.application.ApplicationViewModel
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.feature.connection.factory.DataBrokerConnectorFactory
import org.eclipse.kuksa.companion.feature.connection.repository.ConnectionInfoRepository
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel.ConnectionState
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
import org.eclipse.kuksa.extension.vssProperty.not
import org.eclipse.kuksa.proto.v1.Types.Field
import org.eclipse.kuksa.vss.VssAxle
import org.eclipse.kuksa.vss.VssDoor
import org.eclipse.kuksa.vss.VssHvac
import org.eclipse.kuksa.vss.VssLights
import org.eclipse.kuksa.vss.VssStation
import org.eclipse.kuksa.vss.VssTrunk
import org.eclipse.kuksa.vsscore.annotation.VssDefinition
import org.eclipse.kuksa.vsscore.model.VssSpecification
import javax.inject.Inject

@AndroidEntryPoint
@VssDefinition("vss_rel_4.0.yaml")
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var connectionInfoRepository: ConnectionInfoRepository

    private lateinit var doorVehicleScene: DoorVehicleScene
    private val doorVehicleSurface = DoorVehicleSurface()

    private val applicationViewModel: ApplicationViewModel by viewModels()
    private val connectionStatusViewModel: ConnectionStatusViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels()
    private val doorControlViewModel: DoorControlViewModel by viewModels()
    private val temperatureViewModel: TemperatureViewModel by viewModels()
    private val lightControlViewModel: LightControlViewModel by viewModels()
    private val wheelPressureViewModel: WheelPressureViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val dataBrokerConnectorFactory = DataBrokerConnectorFactory()

    // storing the connection in the Application keeps the Connection alive on orientation changes
    private var dataBrokerConnection: DataBrokerConnection?
        get() = applicationViewModel.dataBrokerConnection
        set(value) {
            applicationViewModel.dataBrokerConnection = value
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
                    connectionStatusViewModel = connectionStatusViewModel,
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

        applicationViewModel.disconnectListener = DisconnectListener {
            applicationViewModel.dataBrokerConnection = null
            connectionStatusViewModel.connectionState = ConnectionState.DISCONNECTED
        }

        connectionStatusViewModel.onClickReconnect = {
            connectToDataBroker {
                subscribe()
            }
        }

        doorControlViewModel.onClickOpenAll = {
            updateSpecification(
                DOOR_ALL_OPEN.row1.driverSide.isOpen,
                DOOR_ALL_OPEN.row1.passengerSide.isOpen,
                DOOR_ALL_OPEN.row2.driverSide.isOpen,
                DOOR_ALL_OPEN.row2.passengerSide.isOpen,
                TRUNK_OPEN.rear.isOpen,
                fields = listOf(Field.FIELD_ACTUATOR_TARGET),
            )
        }

        doorControlViewModel.onClickCloseAll = {
            updateSpecification(
                DOOR_ALL_CLOSED.row1.driverSide.isOpen,
                DOOR_ALL_CLOSED.row1.passengerSide.isOpen,
                DOOR_ALL_CLOSED.row2.driverSide.isOpen,
                DOOR_ALL_CLOSED.row2.passengerSide.isOpen,
                TRUNK_CLOSED.rear.isOpen,
                fields = listOf(Field.FIELD_ACTUATOR_TARGET),
            )
        }

        doorControlViewModel.onClickToggleDoor = { toggledProperty ->
            updateSpecification(!toggledProperty, fields = listOf(Field.FIELD_ACTUATOR_TARGET))
        }

        doorControlViewModel.onClickToggleTrunk = { toggledProperty ->
            updateSpecification(!toggledProperty, fields = listOf(Field.FIELD_ACTUATOR_TARGET))
        }

        temperatureViewModel.onPlannedTemperatureChanged = { plannedTemperature ->
            updatePlannedTemperature(plannedTemperature)
        }

        lightControlViewModel.onClickToggleLight = { vssProperty ->
            updateSpecification(!vssProperty, fields = listOf(Field.FIELD_ACTUATOR_TARGET))
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
            val vssProperties = listOf(
                VssStation.VssRow1.VssDriver.VssTemperature(plannedTemperature),
                VssStation.VssRow1.VssPassenger.VssTemperature(plannedTemperature),
                VssStation.VssRow2.VssDriver.VssTemperature(plannedTemperature),
                VssStation.VssRow2.VssPassenger.VssTemperature(plannedTemperature),
            )

            vssProperties.forEach { vssProperty ->
                dataBrokerConnection?.update(vssProperty, listOf(Field.FIELD_ACTUATOR_TARGET))
            }
        }
    }

    private fun subscribe() {
        dataBrokerConnection?.apply {
            disconnectListeners.register(applicationViewModel.disconnectListener)

            subscribe(VssDoor(), listener = doorControlViewModel.vssDoorListener)
            subscribe(VssTrunk(), listener = doorControlViewModel.vssTrunkListener)
            subscribe(VssHvac(), listener = temperatureViewModel.vssTemperatureListener)
            subscribe(VssAxle(), listener = wheelPressureViewModel.vssWheelPressureListener)
            subscribe(VssLights(), listener = lightControlViewModel.vssLightListener)
        }
    }

    private fun unsubscribe() {
        dataBrokerConnection?.apply {
            disconnectListeners.unregister(applicationViewModel.disconnectListener)

            unsubscribe(VssDoor(), listener = doorControlViewModel.vssDoorListener)
            unsubscribe(VssTrunk(), listener = doorControlViewModel.vssTrunkListener)
            unsubscribe(VssHvac(), listener = temperatureViewModel.vssTemperatureListener)
            unsubscribe(VssAxle(), listener = wheelPressureViewModel.vssWheelPressureListener)
            unsubscribe(VssLights(), listener = lightControlViewModel.vssLightListener)
        }
    }

    private fun updateSpecification(
        vararg specifications: VssSpecification,
        fields: List<Field> = listOf(Field.FIELD_VALUE),
    ) {
        lifecycleScope.launch {
            try {
                specifications.forEach { specifications ->
                    dataBrokerConnection?.update(specifications, fields)
                }
            } catch (e: DataBrokerException) {
                Log.w(TAG, "Failed to update door: $e")
            }
        }
    }

    private fun connectToDataBroker(onConnected: () -> Unit = {}) {
        // dataBrokerConnection is already established e.g. after an orientation change
        if (dataBrokerConnection != null) {
            onConnected()
            return
        }

        lifecycleScope.launch {
            val connectionInfo = connectionInfoRepository.connectionInfoFlow.first()

            try {
                Log.d(TAG, "Connecting to DataBroker ${connectionInfo.host}:${connectionInfo.port}")

                connectionStatusViewModel.connectionState = ConnectionState.CONNECTING
                val context = this@MainActivity
                val dataBrokerConnector = dataBrokerConnectorFactory.create(context, connectionInfo)
                dataBrokerConnection = dataBrokerConnector.connect()
                connectionStatusViewModel.connectionState = ConnectionState.CONNECTED
                onConnected()
            } catch (e: DataBrokerException) {
                Log.w(TAG, "Connection to DataBroker failed: ", e)
                connectionStatusViewModel.connectionState = ConnectionState.DISCONNECTED
            }
        }
    }
}
