package com.nocircle.app.pages.settings.about

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.layout.VerticalColumn
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTabRow
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.navigation.NoRoute
import com.nocircle.compose.resources.NoIcon
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.serialization.Serializable

@Serializable
data object AboutRoute : NoRoute

@Composable
fun AboutPage() {
	NoScaffold(
		topBar = {
			val controller = LocalNavController.current
			NoTopAppBar(
				title = { Text(AppString.ABOUT_TITLE.value()) },
				navigationIcon = if (WindowWidthSizes.isCompact) {
					{
						NoIconButton(
							icon = AppIcon.ArrowBack.value()
						) {
							controller.popBackStack()
						}
					}
				} else null
			)
		}
	) { paddingValues ->
		VerticalColumn(
			modifier = Modifier
				.padding(paddingValues)
		) {
			var selected by remember { mutableStateOf(AboutSubPage.VERSION_UPDATE) }
			NoTabRow(
				selected = selected,
				onSelectedChange = { selected = it },
				items = AboutSubPage.entries
			) { subPage ->
				NoIcon(subPage.icon.value())
				Spacer(modifier = Modifier.width(8.dp))
				Text(subPage.title.value())
			}
		}
	}
}

private enum class AboutSubPage(
	val icon: NoIcon,
	val title: AppString
) {
	VERSION_UPDATE(
		icon = AppIcon.History,
		title = AppString.ABOUT_VERSION_UPDATE,
	),
	THIRD_PARTY_DEPENDENCIES(
		icon = AppIcon.Source,
		title = AppString.ABOUT_THIRD_PARTY_LIBRARY,
	)
}