package com.nocircle.compose.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.nocircle.common.resources.getIcon
import com.nocircle.common.resources.getString
import com.nocircle.compose.material3.NoSnackbarColors
import com.nocircle.compose.material3.NoSnackbarVisuals
import com.nocircle.compose.resources.ComposeIcon
import com.nocircle.compose.resources.ComposeString
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

abstract class NoViewModel : ViewModel() {
	
	protected val snackbarVisualsSharedFlow = MutableSharedFlow<NoSnackbarVisuals>()
	
	suspend fun snackbarCollect(collector: FlowCollector<NoSnackbarVisuals>): Nothing {
		this.snackbarVisualsSharedFlow.collect(collector)
	}
	
	protected suspend fun showNoSnackbar(
		message: String,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(
			NoSnackbarVisuals(
				message,
				actionLabel,
				prefixIcon,
				withDismissAction,
				duration,
				colors
			)
		)
	}
	
	protected suspend fun showNoSnackbar(
		message: StringResource,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(
			NoSnackbarVisuals(
				getString(message),
				actionLabel,
				prefixIcon,
				withDismissAction,
				duration,
				colors
			)
		)
	}
	
	protected suspend fun showNoErrorSnackbar(
		message: String,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
	) {
		this.snackbarVisualsSharedFlow.emit(
			NoSnackbarVisuals(
				message,
				actionLabel,
				prefixIcon,
				withDismissAction,
				duration,
				colors = NoSnackbarColors.ERROR
			)
		)
	}
	
	protected suspend fun showNoErrorSnackbar(
		message: StringResource,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
	) {
		this.snackbarVisualsSharedFlow.emit(
			NoSnackbarVisuals(
				getString(message),
				actionLabel,
				prefixIcon,
				withDismissAction,
				duration,
				colors = NoSnackbarColors.ERROR
			)
		)
	}
	
	protected suspend fun autoShowNoSnackbar(success: Boolean, message: String) {
		if (success) {
			showNoSnackbar(message)
		} else {
			showNoErrorSnackbar(message)
		}
	}
	
	protected suspend inline fun <reified T : Any> networkError(): T {
		this.showNoErrorSnackbar(ComposeString.NETWORK_CONNECT_ERROR.getString())
		return when (T::class) {
			Boolean::class -> false
			Unit::class -> Unit
			else -> error("不支持的类型")
		} as T
	}
}