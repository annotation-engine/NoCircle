package com.nocircle.server.common.expends

import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.Instant as KxInstant

@OptIn(ExperimentalTime::class)
fun KxInstant.toKtInstant(): Instant {
	return Instant.fromEpochMilliseconds(this.toEpochMilliseconds())
}