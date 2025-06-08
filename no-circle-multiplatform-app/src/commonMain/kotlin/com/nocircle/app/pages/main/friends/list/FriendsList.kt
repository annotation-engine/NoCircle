package com.nocircle.app.pages.main.friends.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.device.DeviceType
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendsList(
	isCompat: Boolean,
) {
	NoScaffold(
		topBar = {
			NoTopAppBar(
				actions = {
					FriendsSearch(isCompat)
				},
				contentPadding = if (isCompat) NoTopAppBarDefaults.contentPadding else MediumContentPadding
			)
		}
	) {
	
	}
}

private val MediumContentPadding = PaddingValues(12.dp)

@Composable
private fun FriendsSearch(
	isCompat: Boolean,
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		val viewModel = koinViewModel<FriendsListViewModel>()
		val search by viewModel.search.collectAsState()
		val size by remember(isCompat) {
			derivedStateOf {
				when {
					DeviceType.isDesktop -> if (isCompat) 48.dp else 40.dp
					else -> if (isCompat) 48.dp else 44.dp
				}
			}
		}
		NoTextField(
			value = search,
			onValueChange = viewModel::updateSearch,
			modifier = Modifier
				.weight(1f)
				.height(size),
			prefix = { NoIcon(icon = AppIcon.Search.value) },
			placeholder = {
				Text(
					text = AppString.FriendsSearch.value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
		Spacer(modifier = Modifier.width(if (isCompat) 12.dp else 8.dp))
		var showAddFriendSheet by remember { mutableStateOf(false) }
		NoIconButton(
			icon = AppIcon.Add.value,
			modifier = Modifier.size(size),
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
			containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
		) {
			showAddFriendSheet = true
		}
		if (showAddFriendSheet) {
			AddUserSheet(
				onDismissRequest = {
					showAddFriendSheet = false
				}
			)
		}
	}
}