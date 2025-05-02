package com.nocircle.compose.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

val NoIcon.NoCircleLogo: ImageVector by lazy {
	Builder(
		name = "Logo", defaultWidth = 1024.0.dp, defaultHeight = 1024.0.dp,
		viewportWidth = 1024.0f, viewportHeight = 1024.0f
	).apply {
		path(
			fill = SolidColor(Color(0xFF861300)), stroke = SolidColor(Color(0x00000000)),
			strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
			strokeLineMiter = 4.0f, pathFillType = NonZero
		) {
			moveTo(0.0f, 0.0f)
			horizontalLineToRelative(1024.0f)
			verticalLineToRelative(1024.0f)
			horizontalLineToRelative(-1024.0f)
			close()
		}
		path(
			fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF707070)),
			strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
			strokeLineMiter = 4.0f, pathFillType = NonZero
		) {
			moveTo(0.5f, 0.5f)
			horizontalLineToRelative(1023.0f)
			verticalLineToRelative(1023.0f)
			horizontalLineToRelative(-1023.0f)
			close()
		}
		path(
			fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFFFFFF)),
			strokeLineWidth = 64.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
			strokeLineMiter = 4.0f, pathFillType = NonZero
		) {
			moveTo(512.0f, 512.0f)
			moveToRelative(-288.0f, 0.0f)
			arcToRelative(288.0f, 288.0f, 0.0f, true, true, 576.0f, 0.0f)
			arcToRelative(288.0f, 288.0f, 0.0f, true, true, -576.0f, 0.0f)
		}
		path(
			fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
			strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
			pathFillType = NonZero
		) {
			moveTo(340.0f, 503.4f)
			lineToRelative(257.4f, -257.4f)
			lineToRelative(45.3f, 45.3f)
			lineToRelative(-257.4f, 257.4f)
			close()
		}
		path(
			fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
			strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
			pathFillType = NonZero
		) {
			moveTo(381.7f, 732.4f)
			lineToRelative(257.4f, -257.4f)
			lineToRelative(45.3f, 45.3f)
			lineToRelative(-257.4f, 257.4f)
			close()
		}
		path(
			fill = SolidColor(Color(0xFF861300)), stroke = null, strokeLineWidth = 0.0f,
			strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
			pathFillType = NonZero
		) {
			moveTo(198.1f, 825.4f)
			lineToRelative(257.4f, -257.4f)
			lineToRelative(45.3f, 45.3f)
			lineToRelative(-257.4f, 257.4f)
			close()
		}
		path(
			fill = SolidColor(Color(0xFF861300)), stroke = null, strokeLineWidth = 0.0f,
			strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
			pathFillType = NonZero
		) {
			moveTo(532.5f, 401.4f)
			lineToRelative(257.4f, -257.4f)
			lineToRelative(45.3f, 45.3f)
			lineToRelative(-257.4f, 257.4f)
			close()
		}
	}
		.build()
}

@Preview
@Composable
private fun NoCircleLogoPreview(): Unit {
	Box(modifier = Modifier.padding(12.dp)) {
		Image(
			imageVector = NoIcon.NoCircleLogo,
			contentDescription = null
		)
	}
}
