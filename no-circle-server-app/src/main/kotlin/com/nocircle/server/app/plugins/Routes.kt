package com.nocircle.server.app.plugins

import com.nocircle.server.app.generated.generateRoutes
import com.nocircle.server.common.log.NoLog
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
	routing {
		generateRoutes()
	}
	routingRoot.printTree()
}

private fun RoutingNode.printTree() {
	val rootNode = Node("/")
	this.parseNode(rootNode)
	NoLog.info(buildTreeString(rootNode, "").drop(44))
	NoLog.info("[TOTAL] $count")
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
		this.children.forEach {
			it.parseNode(rootNode)
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
	node.children
		.sortedBy { it.path.lowercase() }
		.forEachIndexed { index, child ->
			var tab = when {
				node.children.lastIndex == index -> "└──"
				tabs.isEmpty() && index == 0 -> "┌──"
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