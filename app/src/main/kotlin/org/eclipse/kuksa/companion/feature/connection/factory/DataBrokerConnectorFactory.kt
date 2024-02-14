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

package org.eclipse.kuksa.companion.feature.connection.factory

import android.content.Context
import io.grpc.ChannelCredentials
import io.grpc.Grpc
import io.grpc.ManagedChannelBuilder
import io.grpc.TlsChannelCredentials
import org.eclipse.kuksa.DataBrokerConnector
import org.eclipse.kuksa.DataBrokerException
import org.eclipse.kuksa.companion.feature.connection.model.ConnectionInfo
import java.io.IOException

class DataBrokerConnectorFactory {
    fun create(
        context: Context,
        connectionInfo: ConnectionInfo,
    ): DataBrokerConnector {
        val isTlsEnabled = connectionInfo.isTlsEnabled
        try {
            return if (isTlsEnabled) {
                createSecureConnector(context, connectionInfo)
            } else {
                createInsecureConnector(connectionInfo)
            }
        } catch (e: IllegalArgumentException) {
            throw DataBrokerException("Can't establish connection to Databroker", e)
        }
    }

    private fun createInsecureConnector(connectionInfo: ConnectionInfo): DataBrokerConnector {
        val host = connectionInfo.host
        val port = connectionInfo.port

        val managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()

        return DataBrokerConnector(managedChannel)
    }

    private fun createSecureConnector(
        context: Context,
        connectionInfo: ConnectionInfo,
    ): DataBrokerConnector {
        val host = connectionInfo.host
        val port = connectionInfo.port

        val certificate = connectionInfo.certificate

        val tlsCredentials: ChannelCredentials
        try {
            val rootCertFile = context.contentResolver.openInputStream(certificate.uri)
            tlsCredentials = TlsChannelCredentials.newBuilder().trustManager(rootCertFile).build()
        } catch (e: IOException) {
            throw DataBrokerException("Could not read certificate: ", e)
        } catch (e: SecurityException) {
            throw DataBrokerException("No Permission to read certificate: ", e)
        }

        val channelBuilder = Grpc.newChannelBuilderForAddress(host, port, tlsCredentials)

        val overrideAuthority = certificate.overrideAuthority
        val hasOverrideAuthority = overrideAuthority.isNotEmpty()
        if (hasOverrideAuthority) {
            channelBuilder.overrideAuthority(overrideAuthority)
        }

        val managedChannel = channelBuilder.build()
        return DataBrokerConnector(managedChannel)
    }
}
