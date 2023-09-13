package study.me.please.ui.components

import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceAround
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Bottom sheet layout for editing a question
 * material3 library crashes due to internal issue - TODO make a switch the moment it works
 */
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ListOptionsBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    topBar: (@Composable () -> Unit)? = null,
    actions: @Composable () -> Unit = {},
    state: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    content: @Composable (paddingValues: PaddingValues) -> Unit = {}
) {
    SimpleBottomSheet(
        sheetContent = {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = SpaceAround
            ) {
                actions()
            }
        },
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        topBar = topBar,
        state = state,
        content = content,
    )
}