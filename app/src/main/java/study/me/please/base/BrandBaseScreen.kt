package study.me.please.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import study.me.please.base.navigation.DefaultAppBarActions
import study.me.please.base.navigation.NavIconType
import study.me.please.base.navigation.NavigationRoot
import study.me.please.data.shared.SharedViewModel

/**
 * Most simple screen for implementing bussiness level logic
 */
@Composable
fun BrandBaseScreen(
    modifier: Modifier = Modifier,
    navIconType: NavIconType = NavIconType.BACK,
    title: String? = null,
    subtitle: String? = null,
    onBackPressed: () -> Boolean = { true },
    actionIcons: (@Composable RowScope.() -> Unit)? = null,
    onNavigationIconClick: (() -> Unit)? = null,
    appBarVisible: Boolean = true,
    containerColor: Color = Color.Transparent,
    contentColor: Color = Color.Transparent,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navController = LocalNavController.current
    val sharedViewModel: SharedViewModel = hiltViewModel<SharedViewModel>()

    val currentUser = sharedViewModel.currentUser.collectAsState()

    val navIconClick: (() -> Unit)? = when {
        navIconType == NavIconType.HOME -> {
            {
                navController?.popBackStack(NavigationRoot.Home.route, inclusive = false)
            }
        }
        onNavigationIconClick != null -> onNavigationIconClick
        else -> null
    }

    BaseScreen(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        onBackPressed = onBackPressed,
        actionIcons = actionIcons ?: {
            DefaultAppBarActions(
                isUserSignedIn = currentUser.value != null,
                userPhotoUrl = currentUser.value?.photoUrl?.toString()
            )
        },
        appBarVisible = appBarVisible,
        containerColor = containerColor,
        contentColor = contentColor,
        onNavigationIconClick = navIconClick,
        floatingActionButtonPosition = floatingActionButtonPosition,
        floatingActionButton = floatingActionButton,
        navigationIcon = navIconType.imageVector,
        content = content
    )
}