package com.nocircle.compose.resources

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.sharp.ArrowForwardIos
import androidx.compose.material.icons.automirrored.twotone.ArrowForwardIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material.icons.twotone.Cancel
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Info
import com.nocircle.common.resources.*

internal enum class ComposeIcon(
	override val rounded: RoundedIcon,
	override val outlined: OutlinedIcon,
	override val filled: FilledIcon,
	override val sharp: SharpIcon,
	override val twoTone: TwoToneIcon,
) : NoIcon {
	Info({ Info }, { Info }, { Info }, { Info }, { Info }),
	Close({ Close }, { Close }, { Close }, { Close }, { Close }),
	Cancel({ Cancel }, { Cancel }, { Cancel }, { Cancel }, { Cancel }),
	ArrowForwardIos(
		rounded = { Icons.AutoMirrored.Rounded.ArrowForwardIos },
		outlined = { Icons.AutoMirrored.Outlined.ArrowForwardIos },
		filled = { Icons.AutoMirrored.Filled.ArrowForwardIos },
		sharp = { Icons.AutoMirrored.Sharp.ArrowForwardIos },
		twoTone = { Icons.AutoMirrored.TwoTone.ArrowForwardIos },
	),
}