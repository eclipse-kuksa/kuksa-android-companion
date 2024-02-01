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

package org.eclipse.kuksa.companion.extension

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import org.eclipse.kuksa.companion.feature.door.view.horizontalMarginAnchorToDoor
import org.eclipse.kuksa.companion.feature.door.view.verticalMarginAnchorToBackDoor
import org.eclipse.kuksa.companion.feature.door.view.verticalMarginAnchorToDoor

fun ConstrainScope.alignDriverFrontDoor(
    windowSizeClass: WindowSizeClass,
    anchorPoint: ConstrainedLayoutReference,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        end.linkTo(anchorPoint.start, horizontalMarginAnchorToDoor)
        top.linkTo(anchorPoint.bottom, verticalMarginAnchorToDoor)
    } else {
        end.linkTo(anchorPoint.start, verticalMarginAnchorToDoor)
        bottom.linkTo(anchorPoint.top, horizontalMarginAnchorToDoor)
    }
}

fun ConstrainScope.alignPassengerFrontDoor(
    windowSizeClass: WindowSizeClass,
    anchorPoint: ConstrainedLayoutReference,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        start.linkTo(anchorPoint.end, horizontalMarginAnchorToDoor)
        top.linkTo(anchorPoint.bottom, verticalMarginAnchorToDoor)
    } else {
        end.linkTo(anchorPoint.start, verticalMarginAnchorToDoor)
        top.linkTo(anchorPoint.bottom, horizontalMarginAnchorToDoor)
    }
}

fun ConstrainScope.alignDriverBackDoor(
    windowSizeClass: WindowSizeClass,
    anchorPoint: ConstrainedLayoutReference,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        end.linkTo(anchorPoint.start, horizontalMarginAnchorToDoor)
        top.linkTo(anchorPoint.bottom, verticalMarginAnchorToBackDoor)
    } else {
        end.linkTo(anchorPoint.start, verticalMarginAnchorToBackDoor)
        bottom.linkTo(anchorPoint.top, horizontalMarginAnchorToDoor)
    }
}

fun ConstrainScope.alignPassengerBackDoor(
    windowSizeClass: WindowSizeClass,
    anchorPoint: ConstrainedLayoutReference,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        start.linkTo(anchorPoint.end, horizontalMarginAnchorToDoor)
        top.linkTo(anchorPoint.bottom, verticalMarginAnchorToBackDoor)
    } else {
        end.linkTo(anchorPoint.start, verticalMarginAnchorToBackDoor)
        top.linkTo(anchorPoint.bottom, horizontalMarginAnchorToDoor)
    }
}

fun ConstrainScope.alignTrunk(
    windowSizeClass: WindowSizeClass,
    anchorPoint: ConstrainedLayoutReference,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        centerHorizontallyTo(anchorPoint)
        bottom.linkTo(parent.bottom)
    } else {
        centerVerticallyTo(anchorPoint)
        start.linkTo(parent.start)
    }
}
