package com.nocircle.compose.complex

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
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
import com.nocircle.compose.foundation.NoIcon

@Composable
fun NoOption(
    title: String,
    subtitle: String? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(MaterialTheme.shapes.small)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.small
            )
            .clickable(
                enabled = onClick != null
            ) {
                onClick!!()
            }
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
        if (actions != null) {
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                actions()
            }
        }
        if (onClick != null) {
            Spacer(modifier = Modifier.width(16.dp))
            NoIcon(
                icon = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}