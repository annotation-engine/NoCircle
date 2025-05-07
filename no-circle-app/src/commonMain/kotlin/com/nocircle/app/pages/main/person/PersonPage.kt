package com.nocircle.app.pages.main.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nocircle.compose.foundation.NoAsyncImage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPage() {
	val viewModel = koinViewModel<PersonViewModel>()
	val verticalScrollState = rememberScrollState()
	Column(
		modifier = Modifier
			.widthIn(max = 600.dp)
			.fillMaxSize()
			.verticalScroll(verticalScrollState)
			.padding(horizontal = 16.dp, vertical = 24.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					color = MaterialTheme.colorScheme.surfaceContainer,
					shape = MaterialTheme.shapes.small
				)
				.padding(24.dp)
				.height(80.dp)
		) {
			val userDetail by viewModel.userDetail.collectAsState()
			NoAsyncImage(
				url = userDetail?.avatarUrl,
				modifier = Modifier
					.size(80.dp)
					.clip(MaterialTheme.shapes.small),
				placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceDim),
				contentScale = ContentScale.Crop
			)
			Spacer(modifier = Modifier.width(12.dp))
			Column(
				modifier = Modifier
					.fillMaxHeight()
					.padding(vertical = 8.dp),
				verticalArrangement = Arrangement.Bottom
			) {
				Text(
					text = userDetail?.nickname ?: "",
					color = MaterialTheme.colorScheme.onSurface,
					fontSize = 18.sp,
					fontWeight = FontWeight.Bold
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = userDetail?.username ?: "",
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
		}
	}
}