package com.nocircle.app.pages.account.login

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
import com.nocircle.app.pages.account.register.RegisterRoute
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.app.resources.AppString
import com.nocircle.app.resources.getString
import com.nocircle.app.resources.value
import com.nocircle.common.expends.not
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoPopUp
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.windowsize.WindowHeightSizes
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.foundation.*
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.showNoSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object LoginRoute : NoRoute

@Composable
fun LoginPage() {
    val viewModel = koinViewModel<LoginViewModel>()
    val hostState = remember { SnackbarHostState() }
    val controller = LocalNavController.current
    LaunchedEffect(Unit) {
        if (controller.resultRoute == RegisterRoute::class) {
            val username = controller.getResult<String>("username")
            if (username != null) {
                viewModel.updateUsername(username)
                hostState.showNoSnackbar(AppString.RegisterSuccess.getString())
            }
        }
        viewModel.snackbarCollect(hostState::showNoSnackbar)
    }
    NoScaffold(
        snackbarHostState = hostState,
    ) { paddingValues ->
        val verticalScroll = rememberScrollState()
        val isCompat = WindowHeightSizes.isCompact || WindowWidthSizes.isCompact
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll),
            contentAlignment = if (isCompat) Alignment.TopCenter else Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 550.dp)
                    .fillMaxSize()
                    .padding(horizontal = 40.dp)
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = AppString.Login.value(),
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(40.dp))

                val username by viewModel.username.collectAsState()
                NoInput(
                    value = username,
                    onValueChange = viewModel::updateUsername,
                    placeholder = AppString.LoginPleaseInputUsername.value(),
                    leadingIcon = { NoIcon(Icons.Outlined.AccountBox) })
                Spacer(modifier = Modifier.height(24.dp))

                val password by viewModel.password.collectAsState()
                val showPassword by viewModel.showPassword.collectAsState()
                NoInput(
                    value = password,
                    onValueChange = viewModel::updatePassword,
                    placeholder = AppString.LoginPleaseInputPassword.value(),
                    leadingIcon = { NoIcon(Icons.Outlined.Lock) },
                    trailingIcon = {
                        NoIconButton(
                            icon = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            shape = CircleShape
                        ) {
                            viewModel.showPassword.not()
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(36.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(36.dp))

                NoButton(
                    text = AppString.Login.value(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val success = viewModel.login()
                    if (success) {
                        launch(Dispatchers.Main) {
                            controller.navigate(
                                route = MainRoute,
                                popup = NoPopUp.All
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                NoButton(
                    text = AppString.LoginToRegister.value(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = NoButtons.SecondaryContainerColors
                ) {
                    controller.navigate(route = RegisterRoute)
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}