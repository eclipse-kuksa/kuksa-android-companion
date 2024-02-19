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

package org.eclipse.kuksa.companion.feature.sheet.view

import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.sidesheet.SideSheetBehavior
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.SHEET_EXPANDED_HEIGHT
import org.eclipse.kuksa.companion.feature.connection.repository.ConnectionInfoRepository
import org.eclipse.kuksa.companion.feature.settings.view.SettingsView
import org.eclipse.kuksa.companion.feature.settings.viewModel.SettingsViewModel

@Composable
fun SideSheetView(
    modifier: Modifier = Modifier,
    isSideSheetEnabled: Boolean = true,
    sheetContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val sideSheetBehavior: SideSheetBehavior<FrameLayout> = SideSheetBehavior<FrameLayout>()
    ConstraintLayout(modifier = modifier) {
        content()

        if (!isSideSheetEnabled) {
            return@ConstraintLayout
        }

        SideSheetInteractionFAB(
            sideSheetBehavior = sideSheetBehavior,
            modifier = Modifier
                .constrainAs(createRef()) {
                    end.linkTo(parent.end, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                },
        )

        SideSheet(
            sheetContent = sheetContent,
            sideSheetBehavior = sideSheetBehavior,
            modifier = Modifier
                .fillMaxHeight()
                .width(SHEET_EXPANDED_HEIGHT.dp)
                .constrainAs(createRef()) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
        )
    }
}

@Composable
private fun SideSheet(
    sideSheetBehavior: SideSheetBehavior<FrameLayout>,
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit,
) {
    // replace with Compose Side-Sheet once it is released:
    // https://m3.material.io/components/side-sheets/overview#32f078ba-e45e-40be-a66b-f04814fabf7a
    AndroidView(
        { context ->
            val composeView = ComposeView(context).apply {
                setContent {
                    Column {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close SideSheet",
                            modifier = Modifier
                                .padding(top = 24.dp, end = 24.dp, bottom = 5.dp)
                                .clickable {
                                    sideSheetBehavior.hide()
                                }
                                .align(Alignment.End),
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(ScrollState(0)),
                        ) {
                            sheetContent()
                        }
                    }
                }
            }

            val frameLayout = FrameLayout(context)
                .apply {
                    background = AppCompatResources.getDrawable(context, R.drawable.rounded_shape)
                    addView(composeView)
                }

            val coordinatorLayout = CoordinatorLayout(context)
                .apply {
                    addView(frameLayout)
                }

            (frameLayout.layoutParams as CoordinatorLayout.LayoutParams)
                .apply {
                    behavior = sideSheetBehavior
                }

            coordinatorLayout
        },
        modifier = modifier,
    )
}

@Composable
private fun SideSheetInteractionFAB(
    sideSheetBehavior: SideSheetBehavior<FrameLayout>?,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = {
            sideSheetBehavior?.state = SideSheetBehavior.STATE_EXPANDED

            when (sideSheetBehavior?.state) {
                SideSheetBehavior.STATE_EXPANDED -> sideSheetBehavior.hide()
                SideSheetBehavior.STATE_HIDDEN -> sideSheetBehavior.expand()
                else -> {
                    // ignored
                }
            }
        },
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_menu_open_24),
            contentDescription = "Open SideSheet",
            modifier = Modifier.size(30.dp),
        )
    }
}

@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
@Composable
private fun SideSheetViewPreview() {
    val context = LocalContext.current
    val repository = ConnectionInfoRepository(context)
    val settingsViewModel = SettingsViewModel(repository)

    SideSheetView(isSideSheetEnabled = true, sheetContent = {}) {
        SettingsView(settingsViewModel = settingsViewModel, Modifier.fillMaxSize())
    }
}
