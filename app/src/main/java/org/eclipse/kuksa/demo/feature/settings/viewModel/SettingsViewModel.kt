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

package org.eclipse.kuksa.demo.feature.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.eclipse.kuksa.demo.feature.connection.model.ConnectionInfo
import org.eclipse.kuksa.demo.feature.connection.repository.ConnectionInfoRepository

class SettingsViewModel(
    private val repository: ConnectionInfoRepository,
) : ViewModel() {
    val connectionInfoFlow = repository.connectionInfoFlow

    fun updateConnectionInfo(connectionInfo: ConnectionInfo) {
        viewModelScope.launch {
            repository.updateConnectionInfo(connectionInfo)
        }
    }

    class Factory(
        private val connectionInfoRepository: ConnectionInfoRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(connectionInfoRepository) as T
        }
    }
}
