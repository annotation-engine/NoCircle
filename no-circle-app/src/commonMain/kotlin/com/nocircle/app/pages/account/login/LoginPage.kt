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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nocircle.app.NoRoute
import com.nocircle.app.generated.resources.*
import com.nocircle.common.expends.getResult
import com.nocircle.common.expends.navigate
import com.nocircle.common.expends.not
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoInput
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbar
import com.nocircle.compose.material3.showNoSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
	navController: NavController,
	hostState: SnackbarHostState = remember { SnackbarHostState() },
	viewModel: LoginViewModel = remember { LoginViewModel(hostState) },
) {
	LaunchedEffect(Unit) {
		val username = navController.getResult<String>("username")
		if (username != null) {
			viewModel.updateUsername(username)
			hostState.showNoSnackbar(Res.string.register_success.value())
		}
	}
	NoScaffold(
		snackbarHost = {
			SnackbarHost(hostState) {
				NoSnackbar(it)
			}
		}
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
			
			val username = viewModel.username.collectAsState()
			NoInput(
				value = username.value,
				onValueChange = viewModel::updateUsername,
				placeholder = Res.string.login_please_input_username.value,
				leadingIcon = { NoIcon(Icons.Outlined.AccountBox) }
			)
			Spacer(modifier = Modifier.height(24.dp))
			
			val password = viewModel.password.collectAsState()
			val showPassword = viewModel.showPassword.collectAsState()
			NoInput(
				value = password.value,
				onValueChange = viewModel::updatePassword,
				placeholder = Res.string.login_please_input_password.value,
				leadingIcon = { NoIcon(Icons.Outlined.Lock) },
				trailingIcon = {
					NoIcon(
						icon = if (showPassword.value) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
						tint = MaterialTheme.colorScheme.primary
					) {
						viewModel.showPassword.not()
					}
				},
				visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation()
			)
			Spacer(modifier = Modifier.height(100.dp))
			
			NoButton(
				text = Res.string.login.value,
				modifier = Modifier.fillMaxWidth(),
				context = Dispatchers.IO
			) {
				val success = viewModel.login()
				if (success) {
					launch(Dispatchers.Main) {
						navController.navigate(route = NoRoute.MAIN, finish = true)
					}
				}
			}
			Spacer(modifier = Modifier.height(24.dp))
			NoButton(
				text = Res.string.login_to_register.value,
				modifier = Modifier.fillMaxWidth(),
				colors = NoButtons.PrimaryContainerColors
			) {
				navController.navigate(NoRoute.ACCOUNT_REGISTER)
			}
			Spacer(modifier = Modifier.height(24.dp))
		}
	}
}