package com.github.ryu1okd.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}


class HealthCheckApiSpec extends WordSpec with Matchers with ScalatestRouteTest with HealthCheckRoutes {

  "HealthCheckRoutes " should {
    "when colling GET '/health_check' should {status: ok}" in {
      Get("/health_check") ~> healthCheckRoutes ~> check {
        status should equal(StatusCodes.OK)
        responseAs[String] shouldEqual "{\"status\":\"ok\"}"
      }
    }
  }
}
