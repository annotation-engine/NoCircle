package com.nocircle.shared.serialization

import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.serializers.FormattedInstantSerializer

object ISOInstantSerializer : FormattedInstantSerializer(
	"com.nocircle.shared.serialization.ISO",
	DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
)