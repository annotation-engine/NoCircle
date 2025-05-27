package com.nocircle.app.pages.main.person.label

import androidx.compose.ui.graphics.Color
import com.nocircle.app.api.impl.labelApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.common.expends.toHexString
import com.nocircle.compose.viewmodel.NoViewModel

class EditLabelViewModel : NoViewModel() {
	
	suspend fun deleteLabelById(id: Int): Boolean {
		val result = ktorfitx.labelApi.deleteLabelById(id) ?: return networkError()
		if (result.success) {
			showNoSnackbar(result.msg)
		} else {
			showNoErrorSnackbar(result.msg)
		}
		return result.success
	}
	
	suspend fun addLabel(label: String, color: Color): Boolean {
		if (label.isBlank()) {
			showNoErrorSnackbar("标签不能为空")
			return false
		}
		val result = ktorfitx.labelApi.addLabel(label, color.toHexString()) ?: return networkError()
		if (result.success) {
			showNoSnackbar(result.msg)
		} else {
			showNoErrorSnackbar(result.msg)
		}
		return result.success
	}
	
	suspend fun updateLabel(id: Int, label: String, color: Color): Boolean {
		if (label.isBlank()) {
			showNoErrorSnackbar("标签不能为空")
			return false
		}
		val result = ktorfitx.labelApi.updateLabel(id, label, color.toHexString()) ?: return networkError()
		return result.success
	}
}