package study.me.please.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.squadris.squadris.compose.components.DEFAULT_ANIMATION_LENGTH_SHORT
import com.squadris.squadris.compose.theme.LocalTheme

/** ScrollBar indicator for [ScrollState] */
@Composable
fun ScrollBarProgressIndicator(
    modifier: Modifier = Modifier,
    totalItems: Int,
    scrollState: LazyListState
) {
    val lastVisibleItemIndex = remember(scrollState) { derivedStateOf {
        scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
    } }
    val scrollLastIndex = remember(totalItems) { Animatable((lastVisibleItemIndex.value ?: 0).toFloat()) }

    LaunchedEffect(lastVisibleItemIndex.value) {
        scrollLastIndex.animateTo(
            lastVisibleItemIndex.value?.toFloat() ?: 0f,
            animationSpec = tween(DEFAULT_ANIMATION_LENGTH_SHORT,)
        )
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = totalItems > 0
    ) {
        LinearProgressIndicator(
            color = LocalTheme.colors.brandMain,
            trackColor = LocalTheme.colors.secondary,
            progress = scrollLastIndex.value.div(totalItems.toFloat()),
            strokeCap = StrokeCap.Round
        )
    }
}