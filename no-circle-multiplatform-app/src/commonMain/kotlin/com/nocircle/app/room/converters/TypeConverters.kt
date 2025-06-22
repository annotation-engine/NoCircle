package com.nocircle.app.room.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

object LocalDateTimeConverter {
	
	@TypeConverter
	fun fromLocalDateTime(value: LocalDateTime): String {
		return Json.encodeToString(value)
	}
	
	@TypeConverter
	fun toLocalDateTime(value: String): LocalDateTime {
		return Json.decodeFromString(value)
	}
}