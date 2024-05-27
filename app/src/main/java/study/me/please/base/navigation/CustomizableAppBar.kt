package study.me.please.base.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squadris.squadris.compose.theme.LocalTheme
import study.me.please.R
import study.me.please.ui.components.AutoResizeText
import study.me.please.ui.components.FontSizeRange

const val AppBarHeightDp = 48

/**
 * Custom app bar with options of customization
 * @param title title of the screen/app
 * @param navigationIcon current icon for navigation back/closing or none in case of null
 * @param actions other actions on the right side of the action bar
 * @param onNavigationIconClick event upon clicking on navigation back
 */
@Composable
fun CustomizableAppBar(
    modifier: Modifier = Modifier,
    title: String? = "Home",
    subtitle: String? = null,
    navigationIcon: Pair<ImageVector, String>? = Icons.Outlined.Home to "",
    actions: @Composable RowScope.() -> Unit = {},
    onNavigationIconClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationIcon?.let { navigationIcon ->
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
                    .clip(LocalTheme.shapes.rectangularActionShape)
                    .clickable(
                        indication = rememberRipple(
                            bounded = true,
                            color = LocalTheme.colors.contrastActionLight
                        ),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onNavigationIconClick()
                    }
                    .padding(8.dp),
                imageVector = navigationIcon.first,
                contentDescription = navigationIcon.second,
                tint = LocalTheme.colors.tetrial
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = AppBarHeightDp.dp),
            verticalArrangement = Arrangement.Center
        ) {
            var fontSizeValue by remember { mutableFloatStateOf(22f) }

            AutoResizeText(
                modifier = Modifier.animateContentSize(),
                text = buildAnnotatedString {
                    if(title != null) {
                        withStyle(SpanStyle(fontSize = fontSizeValue.sp)) {
                            append(title)
                        }
                    }
                    if(subtitle != null) {
                        if(title != null) append("\n")
                        withStyle(SpanStyle(fontSize = fontSizeValue.times(0.65f).sp, fontWeight = FontWeight.Normal)) {
                            append(subtitle)
                        }
                    }
                },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = LocalTheme.colors.tetrial
                ),
                fontSizeRange = FontSizeRange(
                    min = 14.sp,
                    max = 22.sp
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                onFontSizeChange = { fontSize ->
                    fontSizeValue = fontSize
                }
            )
        }
        actions(this)
        Spacer(modifier = Modifier.width(4.dp))
    }
}



@Preview
@Composable
private fun Preview() {
    CustomizableAppBar(
        modifier = Modifier.fillMaxWidth(),
        actions = {
            DefaultAppBarActions()
            ActionBarIcon(
                text = stringResource(id = R.string.button_play),
                imageVector = Icons.Outlined.PlayArrow
            )
        },
        subtitle = "subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle subtitle ",
        title = "title title title title title title title title title title title title title title title "
    )
}