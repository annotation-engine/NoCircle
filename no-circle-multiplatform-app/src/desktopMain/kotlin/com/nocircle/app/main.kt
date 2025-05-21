package com.nocircle.app

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.account.register.RegisterRoute
import com.nocircle.app.pages.guide.GuideRoute
import com.nocircle.common.device.DeviceName
import com.nocircle.common.device.NoDevice
import com.nocircle.common.navigation.NoNavControllerManager
import com.nocircle.common.navigation.NoRoute
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import kotlin.reflect.KClass

fun main() {
	application {
		var widthTarget by remember { mutableStateOf(440.dp) }
		var heightTarget by remember { mutableStateOf(540.dp) }
		val width by animateDpAsState(
			targetValue = widthTarget,
			animationSpec = spring(stiffness = Spring.StiffnessLow)
		)
		var minSizeTarget by remember { mutableStateOf(DpSize(440.dp, 540.dp)) }
		var minSize by remember { mutableStateOf(DpSize(440.dp, 540.dp)) }
		val height by animateDpAsState(
			targetValue = heightTarget,
			animationSpec = spring(stiffness = Spring.StiffnessLow),
			finishedListener = {
				minSize = minSizeTarget
			}
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
			}
			LaunchedEffect(minSize) {
				window.minimumSize = Dimension(
					minSize.width.value.toInt(),
					minSize.height.value.toInt()
				)
			}
			val navController = NoNavControllerManager.get()
			LaunchedEffect(Unit) {
				navController.addOnDestinationChangedListener { controller, _, _ ->
					val currentRoute = controller.currentRoute
					val config = WindowConfig.entries.find { it.routes != null && currentRoute in it.routes } ?: WindowConfig.Other
					widthTarget = config.size.width
					heightTarget = config.size.height
					resizable = config.resize
					minSizeTarget = config.minSize
				}
			}
			NoApp {
				val surface = MaterialTheme.colorScheme.surface
				LaunchedEffect(surface) {
					window.background = surface.let { Color(it.red, it.green, it.blue) }
				}
			}
		}
	}
}

private enum class WindowConfig(
	val size: DpSize,
	val routes: Array<KClass<out NoRoute>>? = null,
	val resize: Boolean,
	val minSize: DpSize
) {
	Size440x540(
		size = DpSize(440.dp, 540.dp),
		routes = arrayOf(GuideRoute::class, LoginRoute::class, RegisterRoute::class),
		resize = false,
		minSize = DpSize(440.dp, 540.dp)
	),
	Other(
		size = DpSize(800.dp, 600.dp),
		resize = true,
		minSize = DpSize(600.dp, 540.dp)
	)
}

private val screenSize by lazy { Toolkit.getDefaultToolkit().screenSize }

private fun calcWindowPosition(width: Dp, height: Dp): WindowPosition {
	val screenWidth = screenSize.width
	val screenHeight = screenSize.height
	return WindowPosition.Absolute(
		x = ((screenWidth - width.value) / 2).dp,
		y = ((screenHeight - height.value) / 2).dp
	)
}