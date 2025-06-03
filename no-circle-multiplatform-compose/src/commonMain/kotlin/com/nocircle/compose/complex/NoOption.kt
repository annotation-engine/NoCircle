package com.nocircle.compose.complex

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.resources.ComposeIcon

@Composable
fun NoOption(
	title: String,
	modifier: Modifier = Modifier,
	subtitle: String? = null,
	action: @Composable (BoxScope.() -> Unit)? = null,
	icon: ImageVector,
	showForwardIcon: Boolean = false,
	onClick: (() -> Unit)? = null
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.height(60.dp)
			.clip(MaterialTheme.shapes.small)
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = MaterialTheme.shapes.small
			)
			.then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
			.padding(horizontal = 16.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		NoIcon(
			icon = icon,
			tint = MaterialTheme.colorScheme.onSurface
		)
		Spacer(modifier = Modifier.width(8.dp))
		Text(
			text = title,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleMedium,
		)
		Spacer(modifier = Modifier.weight(1f))
		if (subtitle != null) {
			Spacer(modifier = Modifier.width(16.dp))
			Text(
				text = subtitle,
				color = MaterialTheme.colorScheme.outline,
				style = MaterialTheme.typography.bodyMedium,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1,
				textAlign = TextAlign.End,
			)
		}
		if (action != null) {
			Spacer(modifier = Modifier.width(32.dp))
			Box(
				modifier = Modifier.fillMaxHeight(),
				contentAlignment = Alignment.CenterStart,
			) {
				action()
			}
		}
		if (onClick != null || showForwardIcon) {
			Spacer(modifier = Modifier.width(16.dp))
			NoIcon(
				icon = ComposeIcon.ArrowForwardIos.value,
				modifier = Modifier.size(20.dp),
				tint = MaterialTheme.colorScheme.outline
			)
		}
	}
}