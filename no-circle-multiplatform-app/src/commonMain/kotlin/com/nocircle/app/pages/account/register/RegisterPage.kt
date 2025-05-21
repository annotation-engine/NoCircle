package com.nocircle.app.pages.account.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nocircle.app.generated.resources.*
import com.nocircle.common.expends.not
import com.nocircle.common.navigation.NoNavControllerManager
import com.nocircle.common.navigation.NoRoute
import com.nocircle.compose.foundation.*
import com.nocircle.compose.foundation.layout.autoPadding
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbarHost
import com.nocircle.compose.material3.showNoSnackbar
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object RegisterRoute : NoRoute

@Composable
fun RegisterPage() {
	val viewModel = koinViewModel<RegisterViewModel>()
	val hostState = remember { SnackbarHostState() }
	LaunchedEffect(Unit) {
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHost = { NoSnackbarHost(hostState) }
	) { paddingValues ->
		val verticalScroll = rememberScrollState()
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.autoPadding(paddingValues)
				.verticalScroll(verticalScroll),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 550.dp)
					.padding(horizontal = 40.dp)
			) {
				Spacer(modifier = Modifier.height(60.dp))
				Text(
					text = Res.string.register.value(),
					style = MaterialTheme.typography.displayLarge
				)
				Spacer(modifier = Modifier.height(40.dp))
				
				val username by viewModel.username.collectAsState()
				NoInput(
					value = username,
					onValueChange = viewModel::updateUsername,
					placeholder = Res.string.register_please_input_username.value(),
					leadingIcon = { NoIcon(Icons.Outlined.AccountBox) }
				)
				Spacer(modifier = Modifier.height(24.dp))
				
				val password by viewModel.password.collectAsState()
				val showPassword = viewModel.showPassword.collectAsState()
				NoInput(
					value = password,
					onValueChange = viewModel::updatePassword,
					placeholder = Res.string.register_please_input_password.value(),
					leadingIcon = { NoIcon(Icons.Outlined.Lock) },
					trailingIcon = {
						NoIconButton(
							icon = if (showPassword.value) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
							tint = MaterialTheme.colorScheme.primary,
							shape = CircleShape
						) {
							viewModel.showPassword.not()
						}
					},
					visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation()
				)
				Spacer(modifier = Modifier.height(24.dp))
				
				val confirmPassword = viewModel.confirmPassword.collectAsState()
				val showConfirmPassword = viewModel.showConfirmPassword.collectAsState()
				NoInput(
					value = confirmPassword.value,
					onValueChange = viewModel::updateConfirmPassword,
					placeholder = Res.string.register_please_confirm_password.value(),
					leadingIcon = { NoIcon(Icons.Outlined.Lock) },
					trailingIcon = {
						NoIconButton(
							icon = if (showConfirmPassword.value) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
							tint = MaterialTheme.colorScheme.primary,
							shape = CircleShape
						) {
							viewModel.showConfirmPassword.not()
						}
					},
					visualTransformation = if (showConfirmPassword.value) VisualTransformation.None else PasswordVisualTransformation()
				)
				
				Spacer(modifier = Modifier.height(36.dp))
				HorizontalDivider()
				Spacer(modifier = Modifier.height(36.dp))
				
				val navController = NoNavControllerManager.get()
				NoButton(
					text = Res.string.register.value(),
					modifier = Modifier.fillMaxWidth()
				) {
					val success = viewModel.register()
					if (success) {
						launch(Dispatchers.Main) {
							navController.popBackStack("username" to username)
						}
					}
				}
				Spacer(modifier = Modifier.height(24.dp))
				NoButton(
					text = Res.string.register_back_to_login.value(),
					modifier = Modifier.fillMaxWidth(),
					colors = NoButtons.SecondaryContainerColors
				) {
					navController.popBackStack()
				}
				Spacer(modifier = Modifier.height(60.dp))
			}
		}
	}
}