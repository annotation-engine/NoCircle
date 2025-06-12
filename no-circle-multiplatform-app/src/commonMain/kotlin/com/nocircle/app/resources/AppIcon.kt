package com.nocircle.app.resources

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.automirrored.twotone.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.sharp.*
import androidx.compose.material.icons.twotone.*
import com.nocircle.common.resources.autoMirroredIconGroup
import com.nocircle.common.resources.iconGroup

object AppIcon {
	
	val AccountBox = iconGroup({ AccountBox }, { AccountBox }, { AccountBox }, { AccountBox }, { AccountBox })
	val Lock = iconGroup({ Lock }, { Lock }, { Lock }, { Lock }, { Lock })
	val Visibility = iconGroup({ Visibility }, { Visibility }, { Visibility }, { Visibility }, { Visibility })
	val VisibilityOff = iconGroup({ VisibilityOff }, { VisibilityOff }, { VisibilityOff }, { VisibilityOff }, { VisibilityOff })
	val LightMode = iconGroup({ LightMode }, { LightMode }, { LightMode }, { LightMode }, { LightMode })
	val DarkMode = iconGroup({ DarkMode }, { DarkMode }, { DarkMode }, { DarkMode }, { DarkMode })
	val KeyboardDoubleArrowRight = iconGroup({ KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight })
	val Home = iconGroup({ Home }, { Home }, { Home }, { Home }, { Home })
	val Group = iconGroup({ Group }, { Group }, { Group }, { Group }, { Group })
	val Diversity2 = iconGroup({ Diversity2 }, { Diversity2 }, { Diversity2 }, { Diversity2 }, { Diversity2 })
	val Person = iconGroup({ Person }, { Person }, { Person }, { Person }, { Person })
	val Add = iconGroup({ Add }, { Add }, { Add }, { Add }, { Add })
	val Remove = iconGroup({ Remove }, { Remove }, { Remove }, { Remove }, { Remove })
	val AccessTime = iconGroup({ AccessTime }, { AccessTime }, { AccessTime }, { AccessTime }, { AccessTime })
	val Settings = iconGroup({ Settings }, { Settings }, { Settings }, { Settings }, { Settings })
	val Edit = iconGroup({ Edit }, { Edit }, { Edit }, { Edit }, { Edit })
	val Cookie = iconGroup({ Cookie }, { Cookie }, { Cookie }, { Cookie }, { Cookie })
	val Language = iconGroup({ Language }, { Language }, { Language }, { Language }, { Language })
	val Warning = iconGroup({ Warning }, { Warning }, { Warning }, { Warning }, { Warning })
	val Contrast = iconGroup({ Contrast }, { Contrast }, { Contrast }, { Contrast }, { Contrast })
	val ColorLens = iconGroup({ ColorLens }, { ColorLens }, { ColorLens }, { ColorLens }, { ColorLens })
	val Search = iconGroup({ Search }, { Search }, { Search }, { Search }, { Search })
	val ShapeLine = iconGroup({ ShapeLine }, { ShapeLine }, { ShapeLine }, { ShapeLine }, { ShapeLine })
	val Memory = iconGroup({ Memory }, { Memory }, { Memory }, { Memory }, { Memory })
	val GroupAdd = iconGroup({ GroupAdd }, { GroupAdd }, { GroupAdd }, { GroupAdd }, { GroupAdd })
	val Email = iconGroup({ Email }, { Email }, { Email }, { Email }, { Email })
	val MarkEmailUnread = iconGroup({ MarkEmailUnread }, { MarkEmailUnread }, { MarkEmailUnread }, { MarkEmailUnread }, { MarkEmailUnread })
	
	val KeyboardArrowRight = autoMirroredIconGroup({ KeyboardArrowRight }, { KeyboardArrowRight }, { KeyboardArrowRight }, { KeyboardArrowRight }, { KeyboardArrowRight })
	val ArrowBack = autoMirroredIconGroup({ ArrowBack }, { ArrowBack }, { ArrowBack }, { ArrowBack }, { ArrowBack })
}