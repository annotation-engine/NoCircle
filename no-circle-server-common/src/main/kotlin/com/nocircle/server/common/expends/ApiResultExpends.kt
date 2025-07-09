package com.nocircle.server.common.expends

import com.nocircle.server.common.model.Code
import com.nocircle.shared.model.ApiResult

inline fun <reified T : Any, C : Code> ApiResult.Companion.create(
	data: T,
	code: C
): ApiResult<T> = ApiResult(code.code, code.msg, data)

inline fun <reified T : Any, C : Code> ApiResult.Companion.create(
	code: C
): ApiResult<T> = ApiResult(code.code, code.msg, null)