package com.nocircle.compose.resources

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

internal object ComposeIcon {
	
	val ArrowForwardIos = autoMirroredIcons({ ArrowForwardIos }, { ArrowForwardIos }, { ArrowForwardIos }, { ArrowForwardIos }, { ArrowForwardIos })
	val Cancel = icons({ Cancel }, { Cancel }, { Cancel }, { Cancel }, { Cancel })
	val Close = icons({ Close }, { Close }, { Close }, { Close }, { Close })
	val Info = icons({ Info }, { Info }, { Info }, { Info }, { Info })
}