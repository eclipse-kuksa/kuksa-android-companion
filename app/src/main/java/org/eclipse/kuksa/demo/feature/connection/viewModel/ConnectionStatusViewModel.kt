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

package org.eclipse.kuksa.demo.feature.connection.viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import org.eclipse.kuksa.demo.extension.DarkGreen

class ConnectionStatusViewModel : ViewModel() {

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
    }

    var onClickReconnect: () -> Unit = { }

    var connectionState by mutableStateOf(ConnectionState.DISCONNECTED)

    val backgroundColor by derivedStateOf {
        when (connectionState) {
            ConnectionState.CONNECTED -> Color.DarkGreen

            ConnectionState.CONNECTING,
            ConnectionState.DISCONNECTED,
            -> Color.Red
        }
    }
}
