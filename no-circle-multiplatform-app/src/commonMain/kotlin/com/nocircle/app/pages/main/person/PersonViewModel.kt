package com.nocircle.app.pages.main.person

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.UserDetailVO
import com.nocircle.app.api.impl.userApi
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.global_network_connect_error
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonViewModel : NoViewModel() {
	
	private val _userDetail = MutableStateFlow<UserDetailVO?>(null)
	val userDetail = _userDetail.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadUserDetail()
		}
	}
	
	private suspend fun loadUserDetail() {
		val result = ktorfitx.userApi.getDetail() ?: let {
			showNoErrorSnackbar(Res.string.global_network_connect_error)
			return
		}
		if (result.success) {
			_userDetail.value = result.data!!
		}
	}
}