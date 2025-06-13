package com.nocircle.app.websockets

import com.nocircle.app.api.impls.keepAliveApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.log.NoLog
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object WebSocketScheduler {
	
	private val sharedFlowMap = mutableMapOf<WebSocketType, MutableSharedFlow<WebSocketReceiver>>()
	private val jobsMap = mutableMapOf<WebSocketType, MutableList<Job>>()
	
	suspend fun keepAlive() {
		ktorfitx.keepAliveApi.keepAlive {
			for (frame in incoming) {
				when (frame) {
					is Frame.Close -> break
					is Frame.Text -> handleIncomingFrame(frame)
					else -> continue
				}
			}
		}
	}
	
	inline fun <reified T : Any> register(
		type: WebSocketType,
		scope: CoroutineScope,
		context: CoroutineContext = EmptyCoroutineContext,
		noinline onReceiver: suspend (senderId: Int, data: T) -> Unit
	) = register(type, serializer<T>(), scope, context, onReceiver)
	
	fun <T> register(
		type: WebSocketType,
		deserializer: DeserializationStrategy<T>,
		scope: CoroutineScope,
		context: CoroutineContext = EmptyCoroutineContext,
		onReceiver: suspend (senderId: Int, data: T) -> Unit
	) {
		val sharedFlow = sharedFlowMap.getOrPut(type) { MutableSharedFlow() }
		val jobs = jobsMap.getOrPut(type) { mutableListOf() }
		jobs += scope.launch(context) {
			sharedFlow.collect {
				val data = Json.decodeFromString(deserializer, it.data)
				onReceiver(it.senderId, data)
			}
		}
	}
	
	fun <T> register(
		type: WebSocketType,
		scope: CoroutineScope,
		context: CoroutineContext = EmptyCoroutineContext,
		onReceiver: suspend (senderId: Int) -> Unit
	) {
		val sharedFlow = sharedFlowMap.getOrPut(type) { MutableSharedFlow() }
		val jobs = jobsMap.getOrPut(type) { mutableListOf() }
		jobs += scope.launch(context) {
			sharedFlow.collect {
				onReceiver(it.senderId)
			}
		}
	}
	
	fun unregister(type: WebSocketType) {
		sharedFlowMap -= type
		jobsMap[type]?.forEach { it.cancel() }
		jobsMap -= type
	}
	
	private suspend fun handleIncomingFrame(frame: Frame.Text) {
		val splits = frame.readText().split("::")
		if (splits.size != 3) return
		val senderId = splits[1].toIntOrNull() ?: return
		val webSocketType = WebSocketType.entries.first { it.name == splits[0] }
		val value = WebSocketReceiver(senderId, splits[2])
		NoLog.info("[WS]: $value")
		val sharedFlows = this.sharedFlowMap[webSocketType] ?: return
		sharedFlows.emit(value)
	}
}

data class WebSocketReceiver(
	val senderId: Int,
	val data: String
)

enum class WebSocketType {
	FRIEND_ADD_REQUEST
}