package com.nocircle.server.common.routes

import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.model.NoPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

abstract class RouteContext(
	val path: String
) {
	
	open fun Route.routes() {
	
	}
	
	context(_: AuthContext)
	open fun Route.authenticates() {
	
	}
}

interface AuthContext

private object AuthContextImpl : AuthContext

context(_: AuthContext)
fun ApplicationCall.getPrincipal(): NoPrincipal {
	return this.principal<NoPrincipal>()!!
}

fun ApplicationCall.getPrincipalOrNull(): NoPrincipal? {
	return this.principal<NoPrincipal>()
}

fun Application.routes(vararg group: RouteContext) {
	routing {
		group.forEach {
			with(it) {
				route(it.path) {
					routes()
					authenticate {
						context(AuthContextImpl) {
							authenticates()
						}
					}
				}
			}
		}
	}
	val rootNode = Node("/")
	routingRoot.parseNode(rootNode)
	val tree = buildTreeString(rootNode, "").drop(44)
	NoLog.info("[TOTAL] $count")
	NoLog.info(tree)
}

private fun RoutingNode.parseNode(
	rootNode: Node
) {
	if (this.selector is HttpMethodRouteSelector) {
		val selector = this.selector as HttpMethodRouteSelector
		val method = selector.method
		val paths = this.path.split('/').drop(1)
		var current = rootNode
		paths.forEachIndexed { index, path ->
			if (index < paths.lastIndex) {
				var pathNode = current.children.find { it.path == path }
				if (pathNode == null) {
					pathNode = Node(path)
					current.children += pathNode
				}
				current = pathNode
			} else {
				var node = current.children.find { it.path == path }
				if (node == null) {
					node = Node(path)
					current.children += node
				}
				node.method = method.toString()
				node.auth = this.auth
			}
		}
	} else {
		this.children.forEach { child ->
			child.parseNode(rootNode)
		}
	}
}

private val RoutingNode.auth: Boolean
	get() {
		var isAuthentication = false
		var current: RoutingNode? = this
		while (current != null) {
			if (current.selector is AuthenticationRouteSelector) {
				isAuthentication = true
				break
			}
			current = current.parent
		}
		return isAuthentication
	}

private class Node(
	val path: String,
	var method: String? = null,
	var auth: Boolean? = null,
	val children: MutableList<Node> = mutableListOf()
)

private var count = 0

private fun buildTreeString(node: Node, tabs: String): String = buildString {
	node.children.forEachIndexed { index, child ->
		var tab = when (index) {
			node.children.lastIndex -> "└──"
			else -> "├──"
		}
		append("${" ".repeat(44)}$tabs$tab ${child.path}")
		if (child.method != null) {
			count++
			append(" [${child.method}]")
			if (child.auth == true) {
				append(" *")
			}
		}
		append('\n')
		tab = when (index) {
			node.children.lastIndex -> "    "
			else -> "│   "
		}
		append(buildTreeString(child, tabs + tab))
	}
}