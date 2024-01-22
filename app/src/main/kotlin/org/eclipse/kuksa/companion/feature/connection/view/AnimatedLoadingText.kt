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

package org.eclipse.kuksa.companion.feature.connection.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel.ConnectionState
import kotlin.time.Duration.Companion.milliseconds

private const val MAX_NUMBER_OF_DOTS = 3
private val DelayDuration = 500.milliseconds

@Composable
fun animateLoadingText(
    connectionState: ConnectionState,
    text: String,
): String {
    var animatedText = text
    if (connectionState == ConnectionState.CONNECTING) {
        val numberOfDots = remember {
            mutableIntStateOf(0)
        }
        repeat(numberOfDots.intValue) {
            animatedText += "."
        }
        LaunchedEffect(Unit) {
            while (true) {
                if (numberOfDots.intValue < MAX_NUMBER_OF_DOTS) {
                    numberOfDots.intValue += 1
                } else {
                    numberOfDots.intValue = 0
                }
                delay(DelayDuration)
            }
        }
    }
    return animatedText
}
