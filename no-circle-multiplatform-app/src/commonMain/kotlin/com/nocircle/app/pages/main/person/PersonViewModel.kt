package com.nocircle.app.pages.main.person

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.LabelVO
import com.nocircle.app.api.UserDetailVO
import com.nocircle.app.api.impl.labelApi
import com.nocircle.app.api.impl.userApi
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.global_network_connect_error
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.common.expends.toHexString
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonViewModel : NoViewModel() {
	
	private val _userDetail = MutableStateFlow<UserDetailVO?>(null)
	val userDetail = _userDetail.asStateFlow()
	
	private val _labels = MutableStateFlow<List<LabelVO>?>(null)
	val labels = _labels.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadUserDetail()
			loadLabels()
		}
	}
	
	private suspend fun loadUserDetail() {
		val result = ktorfitx.userApi.queryDetail() ?: let {
			showNoErrorSnackbar(Res.string.global_network_connect_error)
			return
		}
		if (result.success) {
			_userDetail.value = result.data!!
		}
	}
	
	suspend fun loadLabels() {
		val result = ktorfitx.labelApi.queryLabels() ?: return
		if (result.success) {
			_labels.value = result.data!!
		}
	}
	
	suspend fun deleteLabelById(id: Int): Boolean {
		val result = ktorfitx.labelApi.deleteLabelById(id) ?: return false
		if (result.success) {
			showNoSnackbar("标签删除成功")
		} else {
			showNoErrorSnackbar("标签删除失败")
		}
		return result.success
	}
	
	suspend fun addLabel(label: String, color: Color): Boolean {
		if (label.isBlank()) {
			showNoErrorSnackbar("请输入标签")
			return false
		}
		val result = ktorfitx.labelApi.addLabel(label, color.toHexString()) ?: return false
		if (result.success) {
			showNoSnackbar("标签添加成功")
		} else {
			showNoErrorSnackbar("标签添加失败")
		}
		return result.success
	}
}