package com.nocircle.app

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.nocircle.app.theme.NoMaterialTheme
import com.nocircle.common.device.DeviceName
import com.nocircle.common.device.NoDevice
import com.nocircle.common.navigation.currentRoute
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit

fun main() {
	application {
		var widthTarget by remember { mutableStateOf(420.dp) }
		var heightTarget by remember { mutableStateOf(520.dp) }
		val width by animateDpAsState(
			targetValue = widthTarget,
			animationSpec = spring(stiffness = Spring.StiffnessLow)
		)
		val height by animateDpAsState(
			targetValue = heightTarget,
			animationSpec = spring(stiffness = Spring.StiffnessLow)
		)
		var resizable by remember { mutableStateOf(false) }
		Window(
			onCloseRequest = ::exitApplication,
			state = WindowState(
				width = width,
				height = height,
				position = calcWindowPosition(width, height)
			),
			title = "",
			resizable = resizable
		) {
			LaunchedEffect(Unit) {
				if (NoDevice.Name == DeviceName.MacOS) {
					window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
					window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
				}
				window.minimumSize = Dimension(420, 520)
			}
			val navController = NoNavControllers.initAndGetRoot()
			LaunchedEffect(Unit) {
				navController.addOnDestinationChangedListener { it, _, _ ->
					when (it.currentRoute) {
						in WindowSize400x600Routes -> {
							widthTarget = 420.dp
							heightTarget = 520.dp
							resizable = false
						}
						
						else -> {
							widthTarget = 800.dp
							heightTarget = 600.dp
							resizable = true
						}
					}
				}
			}
			NoMaterialTheme {
				val surface = MaterialTheme.colorScheme.surface
				LaunchedEffect(surface) {
					window.background = surface.let { Color(it.red, it.green, it.blue) }
				}
				NoApp()
			}
		}
	}
}

private val WindowSize400x600Routes = arrayOf(
	NoRoutes.Guide::class,
	NoRoutes.Login::class,
	NoRoutes.Register::class
)

private val screenSize by lazy { Toolkit.getDefaultToolkit().screenSize }

private fun calcWindowPosition(width: Dp, height: Dp): WindowPosition {
	val screenWidth = screenSize.width
	val screenHeight = screenSize.height
	return WindowPosition.Absolute(
		x = ((screenWidth - width.value) / 2).dp,
		y = ((screenHeight - height.value) / 2).dp
	)
}