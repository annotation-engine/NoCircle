package com.nocircle.app.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.rootController
import com.nocircle.app.resources.AppString
import com.nocircle.app.resources.value
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoPopUp
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.complex.NoAlertModalBottomSheet
import com.nocircle.compose.complex.NoOption
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.resources.SupportLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object SettingsRoute : NoRoute

@Composable
fun SettingsPage() {
    val controller = LocalNavController.current
    NoScaffold(
        topBar = {
            NoTopAppBar(
                title = { Text(AppString.Settings.value()) },
                navigationIcon = {
                    if (WindowWidthSizes.isCompact) {
                        NoIconButton(
                            icon = Icons.AutoMirrored.Rounded.ArrowBackIos
                        ) {
                            controller.popBackStack()
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        val verticalScrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(verticalScrollState),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 840.dp)
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 32.dp
                    )
            ) {
                NavToAppearance()
                Spacer(modifier = Modifier.height(16.dp))
                SwitchLanguage()
                Spacer(Modifier.height(16.dp))
                Logout()
            }
        }
    }
}

@Composable
private fun NavToAppearance() {
    val controller = LocalNavController.current
    NoOption(
        title = AppString.Appearance.value(),
        subtitle = AppString.SettingsAppearanceSubtitle.value(),
        icon = Icons.Rounded.Cookie
    ) {
        controller.navigate(route = AppearanceRoute)
    }
}

@Composable
private fun SwitchLanguage() {
    val viewModel = koinViewModel<SettingsViewModel>()
    val language by viewModel.language.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    NoOption(
        title = AppString.SettingsSwitchLanguage.value(),
        icon = Icons.Rounded.Language,
        actions = {
            Text(
                text = language.displayName,
                color = MaterialTheme.colorScheme.onSurface
            )
            val coroutineScope = rememberCoroutineScope()
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                SupportLanguage.entries.fastForEach {
                    DropdownMenuItem(
                        text = {
                            Text(it.displayName)
                        },
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.setLanguage(it)
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    ) {
        expanded = true
    }
}

// @Composable
//fun LanguageDropdown(
//    selectedLanguage: String,
//    onLanguageSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    val languageMap = mapOf(
//        "zh" to "中文",
//        "en" to "English"
//    )
//
//    Box {
//        Button(onClick = { expanded = true }) {
//            Text(text = languageMap[selectedLanguage] ?: "选择语言")
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            languageMap.forEach { (code, label) ->
//                DropdownMenuItem(
//                    text = { Text(label) },
//                    onClick = {
//                        expanded = false
//                        onLanguageSelected(code)
//                    }
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logout() {
    var showLogoutModal by remember { mutableStateOf(false) }
    if (showLogoutModal) {
        val viewModel = koinViewModel<SettingsViewModel>()
        NoAlertModalBottomSheet(
            title = {
                Text(AppString.SettingsLogoutTitle.value())
            },
            content = {
                Text(AppString.SettingsLogoutContent.value())
            },
            onDismissRequest = { showLogoutModal = false },
            onConfirm = {
                viewModel.logout()
                rootController?.navigate(
                    route = LoginRoute,
                    popup = NoPopUp.All
                )
            },
            confirmColors = NoButtons.ErrorColors,
            icon = {
                NoIcon(
                    icon = Icons.Rounded.Warning,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(MaterialTheme.shapes.small)
            .background(
                color = MaterialTheme.colorScheme.error
            )
            .clickable {
                showLogoutModal = true
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = AppString.SettingsLogout.value(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onError
        )
    }
}