package com.nocircle.app.pages.main.person.label

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.nocircle.app.api.impls.labelApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.colorToHex
import com.nocircle.common.resources.getString
import com.nocircle.compose.viewmodel.NoViewModel

class EditLabelViewModel : NoViewModel() {
	
	suspend fun deleteLabelById(id: Int): Boolean {
		val result = ktorfitx.labelApi.deleteLabelById(id) ?: return networkError()
		autoShowNoSnackbar(result.success, result.msg)
		return result.success
	}
	
	suspend fun addLabel(label: String, color: Color): Boolean {
		if (label.isBlank()) {
			showNoErrorSnackbar(AppString.LABEL_MUST_NOT_EMPTY.getString())
			return false
		}
		val result = ktorfitx.labelApi.addLabel(label, color.toArgb()) ?: return networkError()
		autoShowNoSnackbar(result.success, result.msg)
		return result.success
	}
	
	suspend fun updateLabel(id: Int, label: String, color: Color): Boolean {
		if (label.isBlank()) {
			showNoErrorSnackbar(AppString.LABEL_MUST_NOT_EMPTY.getString())
			return false
		}
		val result = ktorfitx.labelApi.updateLabel(id, label, colorToHex(color)) ?: return networkError()
		autoShowNoSnackbar(result.success, result.msg)
		return result.success
	}
}