/*
 * Copyright (c) 2023-2024 Contributors to the Eclipse Foundation
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

package org.eclipse.kuksa.companion.feature.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Suppress("SameParameterValue") // re-usability
@Composable
fun SwitchSetting(
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onValueChanged: (Boolean) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onValueChanged(!enabled)
            }
            .padding(SettingsElementPadding),
    ) {
        val (textView, switchView) = createRefs()

        Text(
            text = label,
            fontSize = SettingPrimaryFontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = SettingsTextPaddingEnd)
                .constrainAs(textView) {
                    start.linkTo(parent.start)
                    end.linkTo(switchView.start)

                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
        )
        Switch(
            checked = enabled,
            onCheckedChange = {
                onValueChanged(it)
            },
            modifier = Modifier.constrainAs(switchView) {
                end.linkTo(parent.end)

                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
        )
    }
}

@Composable
@Preview
private fun SwitchSettingPreview() {
    Surface {
        SwitchSetting(label = "Switch Setting", enabled = true) {
            // unused in preview
        }
    }
}
