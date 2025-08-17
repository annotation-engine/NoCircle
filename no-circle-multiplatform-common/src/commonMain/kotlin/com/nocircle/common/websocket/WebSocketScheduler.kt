package com.nocircle.common.websocket

import com.nocircle.shared.websocket.WebSocketType
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object WebSocketScheduler {
	
	private val sharedFlowMap = mutableMapOf<WebSocketType, MutableSharedFlow<WebSocketModel>>()
	private val jobsMap = mutableMapOf<WebSocketType, MutableList<Job>>()
	private val schedulerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
	
	private val defaultInitSharedFlow: () -> MutableSharedFlow<WebSocketModel> = {
		MutableSharedFlow(
			replay = Int.MAX_VALUE,
			extraBufferCapacity = Int.MAX_VALUE
		)
	}
	
	suspend fun scheduleText(
		frame: Frame.Text,
		findWebSocketType: (type: String) -> WebSocketType?
	) {
		val splits = frame.readText().split("::")
		if (splits.size != 2 && splits.size != 3) return
		val senderId = splits[1].toIntOrNull() ?: return
		val webSocketType = findWebSocketType(splits[0]) ?: return
		val data = splits.getOrNull(2)
		val value = WebSocketModelImpl(senderId, data)
		val sharedFlows = this.sharedFlowMap[webSocketType] ?: return
		sharedFlows.emit(value)
	}
	
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
				val data = Json.decodeFromString(deserializer, it.data!!)
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
				val data = Json.decodeFromString(deserializer, it.data!!)
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
}

sealed interface WebSocketModel {
	val senderId: Int
	val data: String?
}

private data class WebSocketModelImpl(
	override val senderId: Int,
	override val data: String?
) : WebSocketModel