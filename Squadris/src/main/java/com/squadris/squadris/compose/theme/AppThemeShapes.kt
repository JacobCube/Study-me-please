package com.squadris.squadris.compose.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/** Styles specific to main app theme [LocalTheme] */
class AppThemeShapes: ThemeShapes {
    override val betweenItemsSpace: Dp = 8.dp
    override val iconSizeSmall: Dp = 32.dp
    override val iconSizeMedium: Dp = 48.dp
    override val iconSizeLarge: Dp = 64.dp
    override val componentCornerRadius: Dp = 16.dp
    override val circularActionShape: Shape = CircleShape
    override val componentShape: Shape = RoundedCornerShape(componentCornerRadius)
}