package com.nocircle.app

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
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
import com.nocircle.compose.animation.animateWindowStateAsState
import com.nocircle.compose.desktop.LocalFrameWindowScope
import java.awt.Dimension
import java.awt.Color as AwtColor

fun main() {
	application {
		var minSize by remember { mutableStateOf(ScreenConfig.EXTRA_SMALL.minSize) }
		var config by remember { mutableStateOf(ScreenConfig.EXTRA_SMALL) }
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
				minSize = minSize
			)
			CompositionLocalProvider(
				LocalDensity provides Density(density = LocalDensity.current.density * 0.88f),
				LocalFrameWindowScope provides this
			) {
				NoApp(
					onDestinationChangedListener = { _, destination, _ ->
						val route = destination.route ?: return@NoApp
						config = ScreenConfig.entries.find { route in it.routes } ?: ScreenConfig.MEDIUM
					},
					onColorSchemeChange = {
						window.background = it.surface.toAwtColor()
					}
				)
			}
		}
	}
}

@Stable
private fun Color.toAwtColor(): AwtColor = AwtColor(this.red, this.green, this.blue, this.alpha)

@Composable
private fun FrameWindowScope.WindowEffect(
	minSize: DpSize
) {
	LaunchedEffect(Unit) {
		if (NoDevice.Name == DeviceName.MACOS) {
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
}

@Immutable
private enum class ScreenConfig(
	val size: DpSize,
	val routes: Array<String>,
	val resizable: Boolean = true,
	val minSize: DpSize = size,
) {
	EXTRA_SMALL(
		size = DpSize(340.dp, 340.dp),
		routes = arrayOf(GuideRoute::class.qualifiedName!!),
		resizable = false
	),
	SMALL(
		size = DpSize(340.dp, 520.dp),
		routes = arrayOf(LoginRoute::class.qualifiedName!!, RegisterRoute::class.qualifiedName!!),
		resizable = false
	),
	MEDIUM(
		size = DpSize(800.dp, 600.dp),
		routes = arrayOf(MainRoute::class.qualifiedName!!),
		minSize = DpSize(380.dp, 540.dp)
	)
}