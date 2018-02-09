package com.github.ryu1okd.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.github.ryu1okd.models.{Memo, MemoJsonProtocol}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class MemosRouteApiSpec extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfterAll with MemoJsonProtocol with MemoRoutes {

  "メモAPI" should {
    "GET /memos は メモJSONリストを返す" in {
      Get("/memos") ~> memoRoute ~> check {
        responseAs[Seq[Memo]] should have length 2
      }
    }
  }
}
