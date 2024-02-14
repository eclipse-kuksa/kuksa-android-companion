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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun EditableTextSetting(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit,
) {
    var isDialogOpen: Boolean by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isDialogOpen = true
            }
            .padding(SettingsElementPadding),
    ) {
        Column(
            Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = label,
                fontSize = SettingPrimaryFontSize,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = value,
                fontSize = SettingSecondaryFontSize,
            )
        }
    }

    if (isDialogOpen) {
        EditTextDialog(
            label = label,
            initialValue = value,
            keyboardType = keyboardType,
            onDismissRequest = {
                isDialogOpen = false
            },
            onClickOk = onValueChanged,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditTextDialog(
    label: String,
    initialValue: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onDismissRequest: () -> Unit = {},
    onClickCancel: () -> Unit = {},
    onClickOk: (String) -> Unit = {},
) {
    val containerColor = MaterialTheme.colorScheme.secondaryContainer

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val direction = LocalLayoutDirection.current
    var textFieldValue by remember {
        val selection = if (direction == LayoutDirection.Ltr) { // move Cursor to end
            TextRange(initialValue.length)
        } else {
            TextRange.Zero
        }

        mutableStateOf(TextFieldValue(initialValue, selection))
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
    ) {
        Box(modifier) {
            val interactionSource = remember { MutableInteractionSource() }
            Column(
                Modifier
                    .background(containerColor, RoundedCornerShape(15.dp))
                    .padding(25.dp),
            ) {
                Text(
                    text = label,
                    fontSize = SettingPrimaryFontSize,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(10.dp))

                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        textFieldValue = newValue
                    },
                    textStyle = TextStyle(
                        fontSize = SettingSecondaryFontSize,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = keyboardType,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onDismissRequest()
                            onClickOk(textFieldValue.text)
                        },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                ) { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = initialValue,
                        enabled = true,
                        innerTextField = innerTextField,
                        interactionSource = interactionSource,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        contentPadding = PaddingValues(0.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        onDismissRequest()
                        onClickCancel()
                    }) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onDismissRequest()
                        onClickOk(textFieldValue.text)
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditableTextSettingPreview() {
    Surface {
        EditableTextSetting(
            label = "Gerätename",
            value = "Pixel",
        ) {
            // unused in preview
        }
    }
}

@Preview
@Composable
private fun EditTextDialogPreview() {
    EditTextDialog(
        label = "Gerätename",
        initialValue = "Pixel 3",
        onDismissRequest = {},
    )
}
