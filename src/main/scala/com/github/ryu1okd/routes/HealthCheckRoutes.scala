package com.github.ryu1okd.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._

trait HealthCheckRoutes {

  lazy val healthCheckRoutes =
    path("health_check") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, "{\"status\":\"ok\"}"))
      }
    }
}
