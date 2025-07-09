package com.nocircle.compose.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nocircle.compose.material3.NoSnackbarColors
import com.nocircle.compose.material3.NoSnackbarVisuals
import com.nocircle.compose.resources.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

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
		message: NoString,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(
			NoSnackbarVisuals(
				message.getString(),
				actionLabel,
				prefixIcon,
				withDismissAction,
				duration,
				colors
			)
		)
	}
	
	protected fun showNoErrorSnackbar(
		message: String,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
	) {
		viewModelScope.launch {
			snackbarVisualsSharedFlow.emit(
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
	}
	
	protected fun showNoErrorSnackbar(
		message: NoString,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = ComposeIcon.Info.getIcon(),
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
	) {
		viewModelScope.launch {
			snackbarVisualsSharedFlow.emit(
				NoSnackbarVisuals(
					message.getString(),
					actionLabel,
					prefixIcon,
					withDismissAction,
					duration,
					colors = NoSnackbarColors.ERROR
				)
			)
		}
	}
	
	protected suspend fun autoShowNoSnackbar(success: Boolean, message: String) {
		if (success) {
			showNoSnackbar(message)
		} else {
			showNoErrorSnackbar(message)
		}
	}
	
	protected inline fun <reified T : Any> networkError(): T {
		this.showNoErrorSnackbar(ComposeString.NETWORK_ERROR.getString())
		return when (T::class) {
			Boolean::class -> false
			Unit::class -> Unit
			else -> error("Not implemented ${T::class}")
		} as T
	}
}