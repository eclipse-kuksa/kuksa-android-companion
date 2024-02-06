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

package org.eclipse.kuksa.companion.feature.home.view.sheet

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.extension.windowSizeClass
import org.eclipse.kuksa.companion.feature.door.view.DoorControlView
import org.eclipse.kuksa.companion.feature.door.view.DoorOverlayView
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetView(
    modifier: Modifier = Modifier,
    initialSheetValue: SheetValue = SheetValue.PartiallyExpanded,
    sheetContent: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val bottomSheetScaffoldState = BottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = initialSheetValue,
            skipPartiallyExpanded = false,
            skipHiddenState = initialSheetValue != SheetValue.Hidden,
        ),
        snackbarHostState = SnackbarHostState(),
    )

    val sheetPeekHeight = if (initialSheetValue == SheetValue.Hidden) 0.dp else BottomSheetDefaults.SheetPeekHeight
    BottomSheetScaffold(

        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            sheetContent()
        },
        sheetPeekHeight = sheetPeekHeight,
        modifier = modifier.fillMaxSize(),
    ) {
        content(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun ExpandedPreview() {
    val application = Application()
    val viewModel = DoorControlViewModel(application)
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    BottomSheetView(initialSheetValue = SheetValue.Expanded, sheetContent = {
        DoorControlView(viewModel = viewModel)
    }) {
        DoorOverlayView(
            viewModel = viewModel,
            windowSizeClass = windowSizeClass,
            modifier = Modifier.padding(it),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun CollapsedPreview() {
    val application = Application()
    val viewModel = DoorControlViewModel(application)
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    BottomSheetView(initialSheetValue = SheetValue.PartiallyExpanded, sheetContent = {
        DoorControlView(viewModel = viewModel)
    }) {
        DoorOverlayView(
            viewModel = viewModel,
            windowSizeClass = windowSizeClass,
            modifier = Modifier.padding(it),
        )
    }
}
