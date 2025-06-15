package com.nocircle.app.pages.main.person.message

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageCenterReceivedRequestList() {
	val viewModel = koinViewModel<MessageCenterViewModel>()
	val requests by viewModel.receivedRequests.collectAsState()
	
}