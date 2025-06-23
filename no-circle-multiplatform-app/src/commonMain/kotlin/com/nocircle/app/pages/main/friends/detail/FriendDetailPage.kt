package com.nocircle.app.pages.main.friends.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.nocircle.app.pages.main.hideBottomNavigationBar
import com.nocircle.app.pages.main.showBottomNavigationBar
import com.nocircle.app.resources.AppIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendDetailPage(
	friendId: Int,
	popBackStack: () -> Unit
) {
	val viewModel = koinViewModel<FriendDetailViewModel>()
	val isCompact = WindowWidthSizes.isCompact
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text("好友详情") },
				navigationIcon = {
					NoIconButton(
						icon = if (isCompact) AppIcon.ArrowBack.value() else AppIcon.Close.value()
					) {
						popBackStack()
					}
				}
			)
		}
	) {
		DisposableEffect(Unit) {
			if (isCompact) {
				hideBottomNavigationBar()
			}
			onDispose {
				showBottomNavigationBar()
			}
		}
	}
}