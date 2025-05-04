package com.nocircle.compose.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.nocircle.compose.material3.NoSnackbarColors
import com.nocircle.compose.material3.NoSnackbarVisuals
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

abstract class NoViewModel : ViewModel() {
	
	protected val snackbarVisualsSharedFlow = MutableSharedFlow<NoSnackbarVisuals>()
	
	suspend fun snackbarCollect(collector: FlowCollector<NoSnackbarVisuals>): Nothing {
		this.snackbarVisualsSharedFlow.collect(collector)
	}
	
	suspend fun showNoSnackbar(
		message: String,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = Icons.Rounded.Info,
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(NoSnackbarVisuals(message, actionLabel, prefixIcon, withDismissAction, duration, colors))
	}
	
	suspend fun showNoSnackbar(
		message: StringResource,
		actionLabel: String? = null,
		prefixIcon: ImageVector? = Icons.Rounded.Info,
		withDismissAction: Boolean = false,
		duration: SnackbarDuration = SnackbarDuration.Short,
		colors: NoSnackbarColors? = null,
	) {
		this.snackbarVisualsSharedFlow.emit(NoSnackbarVisuals(getString(message), actionLabel, prefixIcon, withDismissAction, duration, colors))
	}
}