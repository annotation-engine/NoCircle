package com.nocircle.app.theme.typographies

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.nocircle.app.pages.settings.SettingsViewModel
import org.jetbrains.compose.resources.Font
import org.koin.compose.viewmodel.koinViewModel

val FontWeightList = (100..900 step 100).map { FontWeight(it) }

@Composable
fun getNoTypography(): Typography = with(MaterialTheme.typography) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val fontResource by viewModel.fontResource.collectAsState()
    val fontWeightLevel by viewModel.fontWeightLevel.collectAsState()
    val fontFamily = FontFamily(
        fonts = fontWeightLevel.progression.mapIndexed { index, weight ->
            Font(
                resource = fontResource,
                weight = FontWeightList[index],
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(weight)
                )
            )
        }
    )
    Typography(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}