package com.nocircle.common.expends

import com.nocircle.shared.model.ApiResult

private const val SUCCESS_CODE = 0

val <T : Any> ApiResult<T>.success: Boolean
	get() = this.code == SUCCESS_CODE