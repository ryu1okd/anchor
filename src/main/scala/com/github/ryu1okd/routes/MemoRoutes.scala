package com.github.ryu1okd.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import com.github.ryu1okd.models.{Memo, MemoJsonProtocol, Tag}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.ryu1okd.protocols.MemoTagsProtocol
import com.github.ryu1okd.services.{MemoService, MemoTagService}
import spray.json._

trait MemoRoutes extends MemoJsonProtocol with MemoTagsProtocol {

  lazy val memoRoute =
    pathPrefix("memos") {
      pathEndOrSingleSlash {
        get {
          onSuccess(MemoTagService.find()) { memotag =>
            complete(memotag.map(_.toJson))
          }
        } ~ post {
          entity(as[Memo]) { memo =>
            complete(StatusCodes.Created, MemoService.add(memo))
          }
        }
      } ~ path(LongNumber) { id =>
        get {
          onSuccess(MemoTagService.findByMemoId(id)) {
            case Some(t) => complete(t)
            case None => complete(StatusCodes.NotFound, "")
          }
        } ~ put {
          entity(as[Memo]) {memo =>
            onSuccess(MemoService.update(memo)) {
              case 0 => complete(StatusCodes.Created, MemoService.add(memo))
              case 1 => onSuccess(MemoService.findById(id)) {
                case Some(m) => complete(StatusCodes.OK, m)
                case None => complete(StatusCodes.InternalServerError, "Missing update target record")
              }
            }
          }
        } ~ delete {
          onSuccess(MemoService.delete(id)) {
            case 0 => complete(StatusCodes.NotFound, "")
            case _ => complete(HttpEntity(ContentTypes.`application/json`, "{\"status\": \"ok\"}"))
          }
        }
      }
    }

}
