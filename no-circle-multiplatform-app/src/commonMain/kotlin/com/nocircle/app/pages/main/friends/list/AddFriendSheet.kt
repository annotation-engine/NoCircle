package com.nocircle.app.pages.main.friends.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nocircle.app.resources.AppIcon
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoTextField
import com.nocircle.compose.material3.NoModalBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendSheet(
	onDismissRequest: () -> Unit,
) {
	NoModalBottomSheet(
		onDismissRequest = onDismissRequest,
		icon = { NoIcon(AppIcon.Add.value) },
		title = { Text("添加好友") }
	) {
		val viewModel = koinViewModel<AddFriendSheetViewModel>()
		val search by viewModel.search.collectAsState()
		NoTextField(
			value = search,
			onValueChange = viewModel::updateSearch,
			leadingIcon = { NoIcon(AppIcon.Search.value) },
			placeholder = { Text("搜索用户昵称或者ID") }
		)
	}
}