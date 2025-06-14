package com.nocircle.server.app.routes.message

import com.nocircle.server.app.plugins.MessageContext
import io.ktor.server.routing.*

context(_: MessageContext)
fun Route.query() = get("query") {

}