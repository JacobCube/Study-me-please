package com.squadris.squadris.ext

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.squadris.squadris.compose.theme.LocalTheme

/** Pseudo shimmer effect, animating a brush around an element */
@Composable
fun Modifier.shimmerEffect(
    stripeColor: Color,
    startEndColor: Color,
    shape: Shape = RoundedCornerShape(8.dp)
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val startOffsetX by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = (-1.5 * size.width).toFloat(),
        targetValue = (1.5 * size.width).toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(1000)),
        label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                startEndColor,
                stripeColor,
                startEndColor
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = shape
    ).onGloballyPositioned {
        size = it.size
    }
}

@Composable
fun Modifier.brandShimmerEffect(
    shape: Shape = RoundedCornerShape(8.dp)
): Modifier = composed {
    shimmerEffect(
        stripeColor = LocalTheme.colors.overShimmer,
        startEndColor = LocalTheme.colors.shimmer,
        shape = shape
    )
}

