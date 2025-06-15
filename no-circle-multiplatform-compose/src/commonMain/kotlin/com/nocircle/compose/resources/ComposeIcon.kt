package com.nocircle.compose.resources

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowForwardIos
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
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
import com.nocircle.common.resources.autoMirroredIcons
import com.nocircle.common.resources.icons

internal object ComposeIcon {
	
	val Info = icons({ Info }, { Info }, { Info }, { Info }, { Info })
	val Close = icons({ Close }, { Close }, { Close }, { Close }, { Close })
	val Cancel = icons({ Cancel }, { Cancel }, { Cancel }, { Cancel }, { Cancel })
	
	val ArrowForwardIos = autoMirroredIcons({ ArrowForwardIos }, { ArrowForwardIos }, { ArrowForwardIos }, { ArrowForwardIos }, { ArrowForwardIos })
	val ArrowBack = autoMirroredIcons({ ArrowBack }, { ArrowBack }, { ArrowBack }, { ArrowBack }, { ArrowBack })
}