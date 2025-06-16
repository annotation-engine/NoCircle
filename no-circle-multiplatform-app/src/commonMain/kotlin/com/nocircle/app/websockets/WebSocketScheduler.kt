package com.nocircle.app.websockets

import com.nocircle.app.api.impls.keepAliveApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.log.NoLog
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.pow
import kotlin.time.Duration.Companion.seconds

object WebSocketScheduler {
	
	private val sharedFlowMap = mutableMapOf<WebSocketType, MutableSharedFlow<WebSocketModel>>()
	private val jobsMap = mutableMapOf<WebSocketType, MutableList<Job>>()
	private const val MAX_DELAY_SECONDS = 30
	
	suspend fun keepAlive() {
		var attempt = 0
		while (true) {
			try {
				ktorfitx.keepAliveApi.keepAlive {
					NoLog.info("WebSocket connected.")
					attempt = 0
					for (frame in incoming) {
						when (frame) {
							is Frame.Close -> break
							is Frame.Text -> handleFrameText(frame)
							else -> continue
						}
					}
				}
			} catch (e: Exception) {
				NoLog.error("WebSocket error: ${e.message}.")
			}
			attempt++
			val duration = (2.0.pow(attempt).toInt().coerceIn(0..MAX_DELAY_SECONDS)).seconds
			NoLog.info("WebSocket attempt: $attempt, duration: $duration")
			delay(duration)
		}
	}
	
	private val defaultInitSharedFlow: () -> MutableSharedFlow<WebSocketModel> = {
		MutableSharedFlow(
			replay = Int.MAX_VALUE,
			extraBufferCapacity = Int.MAX_VALUE
		)
	}
	
	private val schedulerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
	
	inline fun <reified T : Any> addGlobalCollect(
		type: WebSocketType,
		noinline initSharedFlow: (() -> MutableSharedFlow<WebSocketModel>)? = null,
		noinline onEvent: suspend (senderId: Int, data: T) -> Unit
	) = addGlobalCollect(type, serializer<T>(), initSharedFlow, onEvent)
	
	fun <T : Any> addGlobalCollect(
		type: WebSocketType,
		deserializer: DeserializationStrategy<T>,
		initSharedFlow: (() -> MutableSharedFlow<WebSocketModel>)? = null,
		onEvent: suspend (senderId: Int, data: T) -> Unit
	) {
		val sharedFlow = sharedFlowMap.getOrPut(type) {
			initSharedFlow?.invoke() ?: defaultInitSharedFlow()
		}
		val jobs = jobsMap.getOrPut(type) { mutableListOf() }
		jobs += schedulerScope.launch {
			sharedFlow.collect {
				val data = Json.decodeFromString(deserializer, it.data)
				onEvent(it.senderId, data)
			}
		}
	}
	
	fun addGlobalCollect(
		type: WebSocketType,
		initSharedFlow: (() -> MutableSharedFlow<WebSocketModel>)? = null,
		onEvent: suspend (senderId: Int) -> Unit
	) {
		val sharedFlow = sharedFlowMap.getOrPut(type) {
			initSharedFlow?.invoke() ?: defaultInitSharedFlow()
		}
		val jobs = jobsMap.getOrPut(type) { mutableListOf() }
		jobs += schedulerScope.launch {
			sharedFlow.collect {
				onEvent(it.senderId)
			}
		}
	}
	
	context(_: CoroutineScope)
	inline fun <reified T : Any> addCollect(
		type: WebSocketType,
		context: CoroutineContext = EmptyCoroutineContext,
		noinline initSharedFlow: (() -> MutableSharedFlow<WebSocketModel>)? = null,
		noinline onEvent: suspend (senderId: Int, data: T) -> Unit
	) = addCollect(type, serializer<T>(), context, initSharedFlow, onEvent)
	
	context(scope: CoroutineScope)
	fun <T : Any> addCollect(
		type: WebSocketType,
		deserializer: DeserializationStrategy<T>,
		context: CoroutineContext = EmptyCoroutineContext,
		initSharedFlow: (() -> MutableSharedFlow<WebSocketModel>)? = null,
		onEvent: suspend (senderId: Int, data: T) -> Unit
	) {
		val sharedFlow = sharedFlowMap.getOrPut(type) {
			initSharedFlow?.invoke() ?: defaultInitSharedFlow()
		}
		val jobs = jobsMap.getOrPut(type) { mutableListOf() }
		jobs += scope.launch(context) {
			sharedFlow.collect {
				val data = Json.decodeFromString(deserializer, it.data)
				onEvent(it.senderId, data)
			}
		}
	}
	
	context(scope: CoroutineScope)
	fun addCollect(
		type: WebSocketType,
		context: CoroutineContext = EmptyCoroutineContext,
		initSharedFlow: (() -> MutableSharedFlow<WebSocketModel>)? = null,
		onEvent: suspend (senderId: Int) -> Unit
	) {
		val sharedFlow = sharedFlowMap.getOrPut(type) {
			initSharedFlow?.invoke() ?: defaultInitSharedFlow()
		}
		val jobs = jobsMap.getOrPut(type) { mutableListOf() }
		jobs += scope.launch(context) {
			sharedFlow.collect {
				onEvent(it.senderId)
			}
		}
	}
	
	fun removeCollects(type: WebSocketType, vararg types: WebSocketType) {
		val types = types.toList() + type
		sharedFlowMap -= types
		types.forEach { type ->
			jobsMap[type]?.forEach { job ->
				if (!job.isCancelled) {
					job.cancel()
				}
			}
			jobsMap -= type
		}
	}
	
	private suspend fun handleFrameText(frame: Frame.Text) {
		val splits = frame.readText().split("::")
		if (splits.size != 3) return
		val senderId = splits[1].toIntOrNull() ?: return
		val webSocketType = WebSocketType.entries.first { it.name == splits[0] }
		val value = WebSocketModel(senderId, splits[2])
		val sharedFlows = this.sharedFlowMap[webSocketType] ?: return
		sharedFlows.emit(value)
		NoLog.info("[WS]: type=$webSocketType, value=$value")
	}
}

data class WebSocketModel(
	val senderId: Int,
	val data: String
)