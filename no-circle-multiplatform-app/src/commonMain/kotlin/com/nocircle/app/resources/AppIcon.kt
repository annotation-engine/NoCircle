package com.nocircle.app.resources

import androidx.compose.material.icons.Icons
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
import com.nocircle.common.resources.*

enum class AppIcon(
	override val rounded: RoundedIcon,
	override val outlined: OutlinedIcon,
	override val filled: FilledIcon,
	override val sharp: SharpIcon,
	override val twoTone: TwoToneIcon
) : NoIcon {
	AccountBox({ AccountBox }, { AccountBox }, { AccountBox }, { AccountBox }, { AccountBox }),
	Lock({ Lock }, { Lock }, { Lock }, { Lock }, { Lock }),
	Visibility({ Visibility }, { Visibility }, { Visibility }, { Visibility }, { Visibility }),
	VisibilityOff({ VisibilityOff }, { VisibilityOff }, { VisibilityOff }, { VisibilityOff }, { VisibilityOff }),
	LightMode({ LightMode }, { LightMode }, { LightMode }, { LightMode }, { LightMode }),
	DarkMode({ DarkMode }, { DarkMode }, { DarkMode }, { DarkMode }, { DarkMode }),
	KeyboardDoubleArrowRight({ KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }, { KeyboardDoubleArrowRight }),
	Home({ Home }, { Home }, { Home }, { Home }, { Home }),
	People({ People }, { People }, { People }, { People }, { People }),
	Diversity2({ Diversity2 }, { Diversity2 }, { Diversity2 }, { Diversity2 }, { Diversity2 }),
	Person({ Person }, { Person }, { Person }, { Person }, { Person }),
	Add({ Add }, { Add }, { Add }, { Add }, { Add }),
	Remove({ Remove }, { Remove }, { Remove }, { Remove }, { Remove }),
	AccessTime({ AccessTime }, { AccessTime }, { AccessTime }, { AccessTime }, { AccessTime }),
	Settings({ Settings }, { Settings }, { Settings }, { Settings }, { Settings }),
	Edit({ Edit }, { Edit }, { Edit }, { Edit }, { Edit }),
	Cookie({ Cookie }, { Cookie }, { Cookie }, { Cookie }, { Cookie }),
	Language({ Language }, { Language }, { Language }, { Language }, { Language }),
	LineWeight({ LineWeight }, { LineWeight }, { LineWeight }, { LineWeight }, { LineWeight }),
	Warning({ Warning }, { Warning }, { Warning }, { Warning }, { Warning }),
	Contrast({ Contrast }, { Contrast }, { Contrast }, { Contrast }, { Contrast }),
	ColorLens({ ColorLens }, { ColorLens }, { ColorLens }, { ColorLens }, { ColorLens }),
	Search({ Search }, { Search }, { Search }, { Search }, { Search }),
	ShapeLine({ ShapeLine }, { ShapeLine }, { ShapeLine }, { ShapeLine }, { ShapeLine }),
	KeyboardArrowRight(
		rounded = { Icons.AutoMirrored.Rounded.KeyboardArrowRight },
		outlined = { Icons.AutoMirrored.Outlined.KeyboardArrowRight },
		filled = { Icons.AutoMirrored.Filled.KeyboardArrowRight },
		sharp = { Icons.AutoMirrored.Sharp.KeyboardArrowRight },
		twoTone = { Icons.AutoMirrored.TwoTone.KeyboardArrowRight },
	),
	ArrowBack(
		rounded = { Icons.AutoMirrored.Rounded.ArrowBack },
		outlined = { Icons.AutoMirrored.Outlined.ArrowBack },
		filled = { Icons.AutoMirrored.Filled.ArrowBack },
		sharp = { Icons.AutoMirrored.Sharp.ArrowBack },
		twoTone = { Icons.AutoMirrored.TwoTone.ArrowBack },
	)
}