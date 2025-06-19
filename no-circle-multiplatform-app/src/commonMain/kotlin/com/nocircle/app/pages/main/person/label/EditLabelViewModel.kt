package com.nocircle.app.pages.main.person.label

import androidx.compose.ui.graphics.Color
import com.nocircle.app.api.impls.labelApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.resources.AppString
import com.nocircle.common.coroutines.FunctionLocker
import com.nocircle.common.coroutines.OnBusyReturnFalse
import com.nocircle.compose.expends.colorToHex
import com.nocircle.compose.resources.getString
import com.nocircle.compose.viewmodel.NoViewModel

class EditLabelViewModel : NoViewModel() {
	
	suspend fun deleteLabelById(id: Int): Boolean {
		return FunctionLocker.tryWithLock(::deleteLabelById, OnBusyReturnFalse) {
			val result = ktorfitx.labelApi.deleteLabelById(id)
				?: return@tryWithLock networkError()
			autoShowNoSnackbar(result.success, result.msg)
			result.success
		}
	}
	
	suspend fun addLabel(label: String, color: Color): Boolean {
		return FunctionLocker.tryWithLock(::addLabel, OnBusyReturnFalse) {
			if (label.isBlank()) {
				showNoErrorSnackbar(AppString.LABEL_MUST_NOT_EMPTY.getString())
				return@tryWithLock false
			}
			val result = ktorfitx.labelApi.addLabel(label, colorToHex(color))
				?: return@tryWithLock networkError()
			autoShowNoSnackbar(result.success, result.msg)
			result.success
		}
	}
	
	suspend fun updateLabel(id: Int, label: String, color: Color): Boolean {
		return FunctionLocker.tryWithLock(::updateLabel, OnBusyReturnFalse) {
			if (label.isBlank()) {
				showNoErrorSnackbar(AppString.LABEL_MUST_NOT_EMPTY.getString())
				return@tryWithLock false
			}
			val result = ktorfitx.labelApi.updateLabel(id, label, colorToHex(color))
				?: return@tryWithLock networkError()
			autoShowNoSnackbar(result.success, result.msg)
			result.success
		}
	}
}