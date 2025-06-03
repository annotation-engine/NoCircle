package com.nocircle.app.pages.account.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.not
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowHeightSizes
import com.nocircle.compose.foundation.*
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.showNoSnackbar
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
		snackbarHostState = hostState,
	) { paddingValues ->
		val verticalScroll = rememberScrollState()
		val isCompat = WindowHeightSizes.isCompact
		Box(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(verticalScroll),
			contentAlignment = if (isCompat) Alignment.TopCenter else Alignment.Center
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 550.dp)
					.padding(horizontal = 40.dp)
			) {
				Spacer(modifier = Modifier.height(80.dp))
				Text(
					text = AppString.Register.value(),
					style = MaterialTheme.typography.displayLarge
				)
				Spacer(modifier = Modifier.height(40.dp))
				
				val username by viewModel.username.collectAsState()
				NoTextField(
					value = username,
					onValueChange = viewModel::updateUsername,
					modifier = Modifier.fillMaxWidth(),
					placeholder = { Text(AppString.RegisterPleaseInputUsername.value()) },
					leadingIcon = { NoIcon(AppIcon.AccountBox.value) }
				)
				Spacer(modifier = Modifier.height(24.dp))
				
				val password by viewModel.password.collectAsState()
				val showPassword = viewModel.showPassword.collectAsState()
				NoTextField(
					value = password,
					onValueChange = viewModel::updatePassword,
					modifier = Modifier.fillMaxWidth(),
					placeholder = { Text(AppString.RegisterPleaseInputPassword.value()) },
					leadingIcon = { NoIcon(AppIcon.Lock.value) },
					trailingIcon = {
						NoIconButton(
							icon = if (showPassword.value) AppIcon.Visibility.value else AppIcon.VisibilityOff.value,
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
				NoTextField(
					value = confirmPassword.value,
					onValueChange = viewModel::updateConfirmPassword,
					modifier = Modifier.fillMaxWidth(),
					placeholder = { Text(AppString.RegisterPleaseConfirmPassword.value()) },
					leadingIcon = { NoIcon(AppIcon.Lock.value) },
					trailingIcon = {
						NoIconButton(
							icon = if (showConfirmPassword.value) AppIcon.Visibility.value else AppIcon.VisibilityOff.value,
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
				
				val controller = LocalNavController.current
				NoButton(
					text = AppString.Register.value(),
					modifier = Modifier.fillMaxWidth()
				) {
					val success = viewModel.register()
					if (success) {
						launch(Dispatchers.Main) {
							controller.popBackStack("username" to username)
						}
					}
				}
				Spacer(modifier = Modifier.height(24.dp))
				NoButton(
					text = AppString.RegisterBackToLogin.value(),
					modifier = Modifier.fillMaxWidth(),
					colors = NoButtons.SecondaryContainerColors
				) {
					controller.popBackStack()
				}
				Spacer(modifier = Modifier.height(80.dp))
			}
		}
	}
}