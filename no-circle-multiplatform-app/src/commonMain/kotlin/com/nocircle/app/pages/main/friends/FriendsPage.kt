package com.nocircle.app.pages.main.friends

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nocircle.app.resources.AppString
import com.nocircle.common.resources.value
import com.nocircle.compose.complex.NoSplitLayout
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.NoTextField
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendsPage() {
    val viewModel = koinViewModel<FriendsViewModel>()
    val contentWidth by viewModel.contentWidth.collectAsState()
    NoSplitLayout(
        contentWidth = contentWidth,
        onContentWidthChange = { viewModel.contentWidth.value = it },
        expend = {

        }
    ) { isCompat ->
        if (isCompat) {

        } else {
            FriendSearchMedium()
        }
    }
}

@Composable
private fun FriendSearchMedium() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 18.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var value by remember { mutableStateOf("") }
        NoTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            prefix = { NoIcon(icon = Icons.Outlined.Search) },
            placeholder = {
                Text(
                    text = AppString.FriendsSearch.value(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        NoIconButton(
            icon = Icons.Filled.Add,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {

        }
    }
}