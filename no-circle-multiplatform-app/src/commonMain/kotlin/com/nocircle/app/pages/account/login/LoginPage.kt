package com.nocircle.app.pages.account.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.nocircle.app.NoRoutes
import com.nocircle.app.generated.resources.*
import com.nocircle.common.expends.not
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoInput
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbarHost
import com.nocircle.compose.material3.showNoSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginPage() {
	val viewModel = koinViewModel<LoginViewModel>()
	val hostState = remember { SnackbarHostState() }
	val navController = NoNavControllerManagers.root
	LaunchedEffect(Unit) {
		if (navController.backRoute == NoRoutes.Register::class) {
			val username = navController.getResult<String>("username")
			if (username != null) {
				viewModel.updateUsername(username)
				hostState.showNoSnackbar(Res.string.register_success)
			}
		}
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
				text = Res.string.login.value,
				style = MaterialTheme.typography.displayLarge
			)
			Spacer(modifier = Modifier.height(40.dp))
			
			val username by viewModel.username.collectAsState()
			NoInput(
				value = username,
				onValueChange = viewModel::updateUsername,
				placeholder = Res.string.login_please_input_username.value,
				leadingIcon = { NoIcon(Icons.Outlined.AccountBox) })
			Spacer(modifier = Modifier.height(24.dp))
			
			val password by viewModel.password.collectAsState()
			val showPassword by viewModel.showPassword.collectAsState()
			NoInput(
				value = password,
				onValueChange = viewModel::updatePassword,
				placeholder = Res.string.login_please_input_password.value,
				leadingIcon = { NoIcon(Icons.Outlined.Lock) },
				trailingIcon = {
					NoIcon(
						icon = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
						tint = MaterialTheme.colorScheme.primary
					) {
						viewModel.showPassword.not()
					}
				},
				visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
			)
			Spacer(modifier = Modifier.height(100.dp))
			
			NoButton(
				text = Res.string.login.value,
				modifier = Modifier.fillMaxWidth()
			) {
				val success = viewModel.login()
				if (success) {
					launch(Dispatchers.Main) {
						navController.navigate(
							route = NoRoutes.Main,
							finish = true
						)
					}
				}
			}
			Spacer(modifier = Modifier.height(24.dp))
			NoButton(
				text = Res.string.login_to_register.value,
				modifier = Modifier.fillMaxWidth(),
				colors = NoButtons.SecondaryContainerColors
			) {
				navController.navigate(route = NoRoutes.Register)
			}
			Spacer(modifier = Modifier.height(100.dp))
		}
	}
}