package com.nocircle.app.page.account.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nocircle.app.AppRoute
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.login
import com.nocircle.app.generated.resources.login_please_input_password
import com.nocircle.app.generated.resources.login_please_input_username
import com.nocircle.app.generated.resources.login_to_register
import com.nocircle.compose.expends.not
import com.nocircle.compose.expends.value
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoInput
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
fun LoginPage(
	navController: NavController,
	hostState: SnackbarHostState = remember { SnackbarHostState() },
	viewModel: LoginViewModel = remember { LoginViewModel(hostState) },
) {
	NoScaffold(
		snackbarHost = {
			SnackbarHost(hostState) {
				NoSnackbar(it)
			}
		}
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 32.dp)
		) {
			Spacer(modifier = Modifier.height(120.dp))
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
			Spacer(modifier = Modifier.height(120.dp))
			
			NoButton(
				text = Res.string.login.value,
				modifier = Modifier.fillMaxWidth(),
				context = Dispatchers.IO
			) {
				val success = viewModel.login()
				if (success) {
					navController.navigate(AppRoute.MAIN) {
						popUpTo(AppRoute.ACCOUNT_LOGIN) {
							inclusive = true
						}
					}
				}
			}
			Spacer(modifier = Modifier.height(24.dp))
			NoButton(
				text = Res.string.login_to_register.value,
				modifier = Modifier.fillMaxWidth(),
				colors = NoButtons.PrimaryContainerColors
			) {
				navController.navigate(AppRoute.ACCOUNT_REGISTER)
			}
		}
	}
}