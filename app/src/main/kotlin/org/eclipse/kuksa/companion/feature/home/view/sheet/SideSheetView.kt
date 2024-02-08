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

import android.graphics.Color
import android.widget.FrameLayout
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.sidesheet.SideSheetBehavior
import org.eclipse.kuksa.companion.R

@Composable
fun SideSheetView(
    modifier: Modifier = Modifier,
    isBottomSheetEnabled: Boolean = true,
    sheetContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var sideSheetBehavior: SideSheetBehavior<FrameLayout>? by remember {
        mutableStateOf(null)
    }
    ConstraintLayout(modifier = modifier) {
        Box(Modifier.fillMaxSize()) {
            val paddingValues = PaddingValues(0.dp)
            content(paddingValues)
        }
        if (isBottomSheetEnabled) {
            SideSheetInteractionFAB(
                sideSheetBehavior = sideSheetBehavior,
                modifier = Modifier
                    .zIndex(2F)
                    .constrainAs(createRef()) {
                        end.linkTo(parent.end, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                    },
            )

            SideSheet(
                sheetContent = sheetContent,
                onSideSheetInitialised = { behavior ->
                    sideSheetBehavior = behavior
                },
                modifier = Modifier
                    .zIndex(3F)
                    .fillMaxHeight()
                    .width(350.dp)
                    .constrainAs(createRef()) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
            )
        }
    }
}

@Composable
private fun SideSheet(
    sheetContent: @Composable () -> Unit,
    onSideSheetInitialised: (SideSheetBehavior<FrameLayout>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sideSheetBehavior: SideSheetBehavior<FrameLayout> = SideSheetBehavior<FrameLayout>()

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
                                }.align(Alignment.End),
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
                .also {
                    it.setBackgroundColor(Color.WHITE)
                }
            frameLayout.addView(composeView)

            val coordinatorLayout = CoordinatorLayout(context)
            coordinatorLayout.addView(frameLayout)

            val layoutParams = frameLayout.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = sideSheetBehavior

            onSideSheetInitialised(sideSheetBehavior)

            coordinatorLayout
        },
        modifier = modifier,
    )
}

@Composable
private fun SideSheetInteractionFAB(sideSheetBehavior: SideSheetBehavior<FrameLayout>?, modifier: Modifier = Modifier) {
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

@Preview
@Composable
private fun SideSheetViewPreview() {
    SideSheetView(isBottomSheetEnabled = true, sheetContent = {}) {}
}
