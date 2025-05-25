package com.nocircle.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.account.register.RegisterRoute
import com.nocircle.app.pages.guide.GuideRoute
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.common.device.DeviceName
import com.nocircle.common.device.NoDevice
import com.nocircle.common.log.NoLog
import com.nocircle.common.navigation.NoNavHostController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.compose.animation.animateWindowStateAsState
import com.nocircle.compose.desktop.LocalFrameWindowScope
import java.awt.Color
import java.awt.Dimension
import kotlin.reflect.KClass

fun main() {
	application {
		var minSize by remember { mutableStateOf(ScreenConfig.Small.minSize) }
		var config by remember { mutableStateOf(ScreenConfig.Small) }
		val windowState by animateWindowStateAsState(
			targetValue = config.size,
			finishedListener = {
				minSize = config.minSize
			}
		)
		Window(
			onCloseRequest = ::exitApplication,
			state = windowState,
			title = "",
			resizable = config.resizable
		) {
			WindowEffect(
				minSize = minSize,
				onConfigChange = { config = it }
			)
			NoLog.info(LocalDensity.current)
			CompositionLocalProvider(
				LocalDensity provides Density(density = LocalDensity.current.density * 0.88f),
				LocalFrameWindowScope provides this
			) {
				NoApp {
					val surface = MaterialTheme.colorScheme.surface
					LaunchedEffect(surface) {
						window.background = surface.let { Color(it.red, it.green, it.blue) }
					}
				}
			}
		}
	}
}

@Composable
private fun FrameWindowScope.WindowEffect(
	minSize: DpSize,
	onConfigChange: (ScreenConfig) -> Unit,
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
	DisposableEffect(rootController) {
		if (rootController == null) {
			return@DisposableEffect onDispose {}
		}
		val listener = NoNavHostController.OnDestinationChangedListener { controller, _, _ ->
			val currentRoute = controller.currentRoute
			val config = ScreenConfig.entries.find { currentRoute in it.routes } ?: ScreenConfig.Medium
			onConfigChange(config)
		}
		rootController!!.addOnDestinationChangedListener(listener)
		onDispose {
			rootController!!.removeOnDestinationChangedListener(listener)
		}
	}
}

private enum class ScreenConfig(
	val size: DpSize,
	val routes: Array<KClass<out NoRoute>>,
	val resizable: Boolean = true,
	val minSize: DpSize = size,
) {
	Small(
		size = DpSize(320.dp, 480.dp),
		routes = arrayOf(GuideRoute::class, LoginRoute::class, RegisterRoute::class),
		resizable = false
	),
	Medium(
		size = DpSize(800.dp, 600.dp),
		routes = arrayOf(MainRoute::class),
		minSize = DpSize(360.dp, 520.dp)
	)
}