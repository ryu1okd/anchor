package com.github.ryu1okd

import scala.util.Properties

object Main extends App {

  val port = sys.env.getOrElse("PORT", "8080").toInt
  WebServer.startServer("0.0.0.0", port)
}
