package com.nocircle.app.resources

import androidx.compose.runtime.Composable
import com.nocircle.app.generated.resources.Res
import com.nocircle.common.expends.format
import com.nocircle.common.log.NoLog
import com.nocircle.compose.resources.NoString
import com.nocircle.compose.resources.getRawString
import com.nocircle.compose.resources.rawValue

enum class AppString : NoString {
    Login,
    LoginPleaseInputUsername,
    LoginPleaseInputPassword,
    LoginToRegister,
    LoginSuccess,

    Register,
    RegisterPleaseInputUsername,
    RegisterPleaseInputPassword,
    RegisterPleaseConfirmPassword,
    RegisterBackToLogin,
    RegisterUsernameLengthAtLeast8,
    RegisterPasswordLengthAtLeast8,
    RegisterPasswordsAreInconsistentTwice,
    RegisterSuccess,

    MainHome,
    MainFriends,
    MainGroups,
    MainPerson,
    MainPrevious,
    MainCollapse,
    MainExpand,

    PersonAccount,
    PersonSettingsSubtitle,

    Settings,
    SettingsAppearanceSubtitle,
    SettingsLogout,
    SettingsLogoutTitle,
    SettingsLogoutContent,
    SettingsSwitchLanguage,

    Appearance,
    AppearanceInUse,
    AppearanceSingleLine,
    AppearanceMultiLine,
    AppearanceContrast,
    AppearanceContrastStandard,
    AppearanceContrastMedium,
    AppearanceContrastHigh,
    AppearanceContrastPreview,
    AppearanceThemeMode,
    AppearanceThemeModeLight,
    AppearanceThemeModeDark,
    AppearanceThemeModeSystem,
    AppearanceThemeModePreview,
    AppearanceTheme,
    AppearanceThemeRed,
    AppearanceThemePurple,
    AppearanceThemeModena,
    AppearanceThemeBlue,
    AppearanceThemeLightBlue,
    AppearanceThemeCyan,
    AppearanceThemeTurquoise,
    AppearanceThemeGreen,
    AppearanceThemeLightGreen,
    AppearanceThemeStoneGray,
    AppearanceThemeYellow,
    AppearanceThemeAmber,
    AppearanceThemePreview
}