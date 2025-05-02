package com.nocircle.common.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.nocircle.common.expends.value
import com.nocircle.common.material3.NoSnackbarColors
import com.nocircle.common.material3.NoSnackbarVisuals
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.compose.resources.StringResource

abstract class NoViewModel : ViewModel() {
	
	protected val snackbarVisualsSharedFlow = MutableSharedFlow<NoSnackbarVisuals>()
	
	suspend fun snackbarCollect(collector: FlowCollector<NoSnackbarVisuals>): Nothing {
		this.snackbarVisualsSharedFlow.collect(collector)
	}
	
	protected suspend fun showNoSnackbar(
		message: String,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = Icons.Rounded.Info,
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(NoSnackbarVisuals(message, actionLabel, prefixIcon, withDismissAction, duration, colors))
	}
	
	protected suspend fun showNoSnackbar(
		message: StringResource,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = Icons.Rounded.Info,
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(NoSnackbarVisuals(message.value(), actionLabel, prefixIcon, withDismissAction, duration, colors))
	}
}