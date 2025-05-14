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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.generated.resources.*
import com.nocircle.common.expends.not
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.*
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbarHost
import com.nocircle.compose.material3.showNoSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterPage() {
	val viewModel = koinViewModel<RegisterViewModel>()
	val hostState = remember { SnackbarHostState() }
	LaunchedEffect(Unit) {
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHost = { NoSnackbarHost(hostState) }
	) {
		val verticalScroll = rememberScrollState()
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(verticalScroll)
				.padding(it)
				.padding(horizontal = 40.dp)
		) {
			Spacer(modifier = Modifier.height(100.dp))
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
			Spacer(modifier = Modifier.height(120.dp))
			
			val navController = NoNavControllerManagers.root
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
			Spacer(modifier = Modifier.height(100.dp))
		}
	}
}