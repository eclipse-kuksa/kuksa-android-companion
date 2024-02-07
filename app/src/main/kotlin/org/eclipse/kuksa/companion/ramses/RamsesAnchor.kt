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

package org.eclipse.kuksa.companion.ramses

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference

private val horizontalMarginAnchorToDoor = 75.dp
private val verticalMarginAnchorToDoor = 10.dp
private val verticalMarginAnchorToBackDoor = 100.dp

sealed class RamsesAnchor(
    protected val windowSizeClass: WindowSizeClass,
    protected val anchorPoint: ConstrainedLayoutReference,
) {
    abstract fun alignCompact(constrainScope: ConstrainScope)

    abstract fun alignMedium(constrainScope: ConstrainScope)

    fun align(constrainScope: ConstrainScope) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> alignCompact(constrainScope)

            WindowWidthSizeClass.Medium,
            WindowWidthSizeClass.Expanded,
            -> alignMedium(constrainScope)
        }
    }
}

class DriverFrontDoorAnchor(windowSizeClass: WindowSizeClass, anchorPoint: ConstrainedLayoutReference) :
    RamsesAnchor(windowSizeClass, anchorPoint) {
    override fun alignCompact(constrainScope: ConstrainScope) {
        constrainScope.apply {
            end.linkTo(anchorPoint.start, horizontalMarginAnchorToDoor)
            top.linkTo(anchorPoint.bottom, verticalMarginAnchorToDoor)
        }
    }

    override fun alignMedium(constrainScope: ConstrainScope) {
        constrainScope.apply {
            end.linkTo(anchorPoint.start, verticalMarginAnchorToDoor)
            bottom.linkTo(anchorPoint.top, horizontalMarginAnchorToDoor)
        }
    }
}

class PassengerFrontDoorAnchor(windowSizeClass: WindowSizeClass, anchorPoint: ConstrainedLayoutReference) :
    RamsesAnchor(windowSizeClass, anchorPoint) {
    override fun alignCompact(constrainScope: ConstrainScope) {
        constrainScope.apply {
            start.linkTo(anchorPoint.end, horizontalMarginAnchorToDoor)
            top.linkTo(anchorPoint.bottom, verticalMarginAnchorToDoor)
        }
    }

    override fun alignMedium(constrainScope: ConstrainScope) {
        constrainScope.apply {
            end.linkTo(anchorPoint.start, verticalMarginAnchorToDoor)
            top.linkTo(anchorPoint.bottom, horizontalMarginAnchorToDoor)
        }
    }
}

class DriverBackDoorAnchor(windowSizeClass: WindowSizeClass, anchorPoint: ConstrainedLayoutReference) :
    RamsesAnchor(windowSizeClass, anchorPoint) {
    override fun alignCompact(constrainScope: ConstrainScope) {
        constrainScope.apply {
            end.linkTo(anchorPoint.start, horizontalMarginAnchorToDoor)
            top.linkTo(anchorPoint.bottom, verticalMarginAnchorToBackDoor)
        }
    }

    override fun alignMedium(constrainScope: ConstrainScope) {
        constrainScope.apply {
            end.linkTo(anchorPoint.start, verticalMarginAnchorToBackDoor)
            bottom.linkTo(anchorPoint.top, horizontalMarginAnchorToDoor)
        }
    }
}

class PassengerBackDoorAnchor(windowSizeClass: WindowSizeClass, anchorPoint: ConstrainedLayoutReference) :
    RamsesAnchor(windowSizeClass, anchorPoint) {
    override fun alignCompact(constrainScope: ConstrainScope) {
        constrainScope.apply {
            start.linkTo(anchorPoint.end, horizontalMarginAnchorToDoor)
            top.linkTo(anchorPoint.bottom, verticalMarginAnchorToBackDoor)
        }
    }

    override fun alignMedium(constrainScope: ConstrainScope) {
        constrainScope.apply {
            end.linkTo(anchorPoint.start, verticalMarginAnchorToBackDoor)
            top.linkTo(anchorPoint.bottom, horizontalMarginAnchorToDoor)
        }
    }
}

class TrunkAnchor(windowSizeClass: WindowSizeClass, anchorPoint: ConstrainedLayoutReference) :
    RamsesAnchor(windowSizeClass, anchorPoint) {
    override fun alignCompact(constrainScope: ConstrainScope) {
        constrainScope.apply {
            centerHorizontallyTo(anchorPoint)
            bottom.linkTo(parent.bottom)
        }
    }

    override fun alignMedium(constrainScope: ConstrainScope) {
        constrainScope.apply {
            centerVerticallyTo(anchorPoint)
            start.linkTo(parent.start)
        }
    }
}

