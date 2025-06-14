package com.nocircle.app.pages.main.person

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.LabelDTO
import com.nocircle.app.api.UserDetailDTO
import com.nocircle.app.api.impls.labelApi
import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonViewModel : NoViewModel() {
	
	private val _userDetail = MutableStateFlow<UserDetailDTO?>(null)
	val userDetail = _userDetail.asStateFlow()
	
	private val _labels = MutableStateFlow<List<LabelDTO>>(emptyList())
	val labels = _labels.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadUserDetail()
			loadLabels()
		}
	}
	
	private suspend fun loadUserDetail(): Boolean {
		val result = ktorfitx.userApi.queryDetail() ?: return networkError()
		if (result.success) {
			_userDetail.value = result.data!!
		}
		return result.success
	}
	
	suspend fun loadLabels() {
		val result = ktorfitx.labelApi.queryLabels() ?: return
		if (result.success) {
			_labels.value = result.data!!
		}
	}
}