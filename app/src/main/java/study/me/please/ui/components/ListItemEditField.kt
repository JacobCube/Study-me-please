package study.me.please.ui.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.squadris.squadris.compose.components.input.EditFieldInput
import com.squadris.squadris.compose.theme.LocalTheme
import study.me.please.R

/** Editable field intended for list of such items */
@Composable
fun ListItemEditField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    prefix: String,
    maxLines: Int = 20,
    hint: String = stringResource(R.string.list_item_generic_hint),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onBackspaceKey: (output: String) -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    val localDensity = LocalDensity.current
    val focusManager = LocalFocusManager.current

    val output = remember(value) { mutableStateOf(value) }
    var fieldLineCount by remember { mutableIntStateOf(1) }

    EditFieldInput(
        modifier = modifier
            .padding(start = 16.dp)
            .wrapContentWidth()
            .animateContentSize()
            .heightIn(min = with(localDensity) {
                LocalTheme.styles.category.fontSize
                    .toDp()
                    .plus(10.dp)
            })
            .height(
                with(localDensity) {
                    LocalTheme.styles.category.fontSize
                        .toDp()
                        .plus(2.dp)
                        .times(fieldLineCount)
                }
            )
            .widthIn(min = TextFieldDefaults.MinWidth)
            .onKeyEvent { keyEvent ->
                Log.d("kostka_test", "key: ${keyEvent.key}")
                when(keyEvent.key) {
                    Key.Tab -> {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                    Key.DirectionUp -> {
                        focusManager.moveFocus(FocusDirection.Up)
                    }
                    Key.DirectionDown -> {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                    Key.Backspace -> {
                        onBackspaceKey(value)
                        true
                    }
                    Key.Enter -> {
                        keyboardActions.onNext?.invoke(object: KeyboardActionScope {
                            override fun defaultKeyboardAction(imeAction: ImeAction) {}
                        })
                        true
                    }
                    else -> false
                }
            },
        value = value,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        hint = hint,
        prefix = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = if(fieldLineCount > 1) Alignment.TopStart else Alignment.Center
            ) {
                Text(
                    text = prefix,
                    style = LocalTheme.styles.category
                )
            }
        },
        textStyle = LocalTheme.styles.category,
        isUnfocusedTransparent = true,
        minLines = 1,
        maxLines = maxLines,
        onTextLayout = { result ->
            fieldLineCount = result.lineCount
            if(result.didOverflowWidth) {
                fieldLineCount++
            }
        },
        paddingValues = PaddingValues(horizontal = 8.dp),
        onValueChange = {
            output.value = it
            onValueChange(it)
        }
    )
}