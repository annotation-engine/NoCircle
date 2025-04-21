package com.nocircle.app.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.RepeatOne
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoInput
import com.nocircle.compose.material3.NoScaffold

@Composable
fun AccountPage(
	navController: NavController
) {
	NoScaffold {
		var type by remember { mutableStateOf(AccountType.Login) }
		AnimatedContent(
			targetState = type,
			transitionSpec = {
				when (type) {
					AccountType.Login -> SlideArrowRight
					AccountType.Register -> SlideArrowLeft
				}
			}
		) {
			when (it) {
				AccountType.Login -> {
					Login { type = AccountType.Register }
				}
				AccountType.Register -> {
					Register { type = AccountType.Login }
				}
			}
		}
	}
}

private val SlideArrowLeft = slideInHorizontally { it } + fadeIn() togetherWith
		slideOutHorizontally { -it } + fadeOut()

private val SlideArrowRight = slideInHorizontally { -it } + fadeIn() togetherWith
		slideOutHorizontally { it } + fadeOut()

private enum class AccountType {
	
	Login,
	
	Register
}

@Composable
private fun Login(
	toRegister: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 32.dp)
	) {
		var username by remember { mutableStateOf("") }
		var password by remember { mutableStateOf("") }
		
		Spacer(modifier = Modifier.height(100.dp))
		
		Text(
			text = "登录",
			style = MaterialTheme.typography.displayMedium
		)
		
		Spacer(modifier = Modifier.height(32.dp))
		NoInput(
			value = username,
			onValueChange = { username = it },
			placeholder = "请输入用户名",
			leadingIcon = { NoIcon(Icons.Outlined.AccountBox) }
		)
		
		Spacer(modifier = Modifier.height(24.dp))
		
		NoInput(
			value = password,
			onValueChange = { password = it },
			placeholder = "请输入密码",
			leadingIcon = { NoIcon(Icons.Outlined.Lock) },
			trailingIcon = { NoIcon(Icons.Outlined.AccountBox) }
		)
		
		Spacer(modifier = Modifier.height(100.dp))
		
		NoButton(
			text = "登录",
			modifier = Modifier.fillMaxWidth()
		) {
		
		}
		
		Spacer(modifier = Modifier.height(24.dp))
		
		NoButton(
			text = "去注册",
			modifier = Modifier.fillMaxWidth(),
			colors = NoButtons.colors(
				containerColor = MaterialTheme.colorScheme.primaryContainer,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer
			)
		) {
			toRegister()
		}
	}
}

@Composable
private fun Register(
	backLogin: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 32.dp),
	) {
		var username by remember { mutableStateOf("") }
		var password by remember { mutableStateOf("") }
		var confirmPassword by remember { mutableStateOf("") }
		
		Spacer(modifier = Modifier.height(100.dp))
		
		Text(
			text = "注册",
			style = MaterialTheme.typography.displayMedium
		)
		
		Spacer(modifier = Modifier.height(32.dp))
		NoInput(
			value = username,
			onValueChange = { username = it },
			placeholder = "请输入用户名",
			leadingIcon = { NoIcon(Icons.Outlined.AccountBox) }
		)
		
		Spacer(modifier = Modifier.height(24.dp))
		
		NoInput(
			value = password,
			onValueChange = { password = it },
			placeholder = "请输入密码",
			leadingIcon = { NoIcon(Icons.Outlined.Lock) },
			trailingIcon = { NoIcon(Icons.Outlined.AccountBox) }
		)
		
		Spacer(modifier = Modifier.height(24.dp))
		
		NoInput(
			value = confirmPassword,
			onValueChange = { confirmPassword = it },
			placeholder = "请重新输入密码",
			leadingIcon = { NoIcon(Icons.Outlined.RepeatOne) },
			trailingIcon = { NoIcon(Icons.Outlined.AccountBox) }
		)
		
		Spacer(modifier = Modifier.height(100.dp))
		
		NoButton(
			text = "注册",
			modifier = Modifier.fillMaxWidth()
		) {
		
		}
		
		Spacer(modifier = Modifier.height(24.dp))
		
		NoButton(
			text = "返回登录",
			modifier = Modifier.fillMaxWidth(),
			colors = NoButtons.colors(
				containerColor = MaterialTheme.colorScheme.primaryContainer,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer
			)
		) {
			backLogin()
		}
	}
}