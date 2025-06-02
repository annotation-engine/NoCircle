package com.nocircle.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.desktop.NoWindowDraggableArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    colors: NoTopAppBarColors = NoTopAppBarDefaults.topAppBarColors
) {
    val paddingValues = NoTopAppBarDefaults.windowInsets.asPaddingValues()
    val isCompat = WindowWidthSizes.isCompact
    val deviceType = NoDevice.Type
    val windowInsets by remember(paddingValues, isCompat) {
        derivedStateOf {
            WindowInsets(
                top = when {
                    !isCompat -> paddingValues.calculateTopPadding() / 2
                    deviceType == DeviceType.Desktop -> paddingValues.calculateTopPadding() + 16.dp
                    else -> paddingValues.calculateTopPadding()
                }
            )
        }
    }
    NoWindowDraggableArea {
        Row(
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = colors.shadowColor,
                    spotColor = colors.shadowColor,
                )
                .fillMaxWidth()
                .background(colors.containerColor)
                .windowInsetsPadding(windowInsets)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (navigationIcon != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.navigationIconContentColor,
                    content = navigationIcon
                )
                Spacer(Modifier.width(8.dp))
            }
            CompositionLocalProvider(
                LocalContentColor provides colors.titleContentColor,
                LocalTextStyle provides MaterialTheme.typography.titleLarge,
                content = title
            )
            Spacer(Modifier.weight(1f))
            if (actions != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.actionIconContentColor
                ) {
                    actions()
                }
            }
        }
    }
}

object NoTopAppBarDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    val windowInsets: WindowInsets
        @Composable
        get() = TopAppBarDefaults.windowInsets

    val topAppBarColors: NoTopAppBarColors
        @Composable
        get() = NoTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            shadowColor = MaterialTheme.colorScheme.onSurface
        )
}

data class NoTopAppBarColors(
    val containerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionIconContentColor: Color,
    val shadowColor: Color
)