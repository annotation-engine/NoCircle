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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.person_account
import com.nocircle.common.expends.hexToColor
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoAsyncImage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonPage() {
	val verticalScrollState = rememberScrollState()
	Column(
		modifier = Modifier
			.widthIn(max = 600.dp)
			.fillMaxSize()
			.verticalScroll(verticalScrollState)
			.padding(horizontal = 16.dp, vertical = 32.dp)
	) {
		UserDetailCard()
	}
}

@Composable
private fun UserDetailCard() {
	val viewModel = koinViewModel<PersonViewModel>()
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
				.padding(vertical = 4.dp),
		) {
			Text(
				text = userDetail?.nickname ?: "",
				color = MaterialTheme.colorScheme.onSurface,
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "${Res.string.person_account.value}${userDetail?.username}",
				color = MaterialTheme.colorScheme.outline,
				fontSize = 12.sp
			)
			Spacer(modifier = Modifier.height(2.dp))
			Row(
				modifier = Modifier
					.height(24.dp)
					.background(
						color = if (userDetail == null) MaterialTheme.colorScheme.surface else Color.Transparent,
						shape = MaterialTheme.shapes.extraSmall
					)
			) {
				if (userDetail == null) {
					Box(
						modifier = Modifier
							.fillMaxHeight()
							.width(80.dp)
							.background(
								color = MaterialTheme.colorScheme.surface,
								shape = MaterialTheme.shapes.extraSmall
							)
					)
				} else {
					val labels = userDetail!!.labels.toList()
					if (labels.isEmpty()) {
						// 添加标签
					} else {
						labels.fastForEachIndexed { index, (label, color) ->
							Box(
								modifier = Modifier
									.fillMaxHeight()
									.background(
										color = remember(color) { color.hexToColor() },
										shape = MaterialTheme.shapes.extraSmall
									)
									.padding(horizontal = 4.dp),
								contentAlignment = Alignment.Center
							) {
								Text(
									text = label,
									color = Color.White,
									fontSize = 12.sp
								)
							}
							if (index < labels.size - 1) {
								Spacer(modifier = Modifier.width(4.dp))
							}
						}
					}
				}
			}
		}
	}
}