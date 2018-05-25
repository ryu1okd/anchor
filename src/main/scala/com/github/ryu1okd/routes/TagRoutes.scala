package com.github.ryu1okd.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import com.github.ryu1okd.models.{Tag, TagJsonProtocol, Tags}
import akka.http.scaladsl.server.Directives._
import com.github.ryu1okd.services.TagService

trait TagRoutes extends TagJsonProtocol {

  lazy val tagRoute =
    pathPrefix("tags") {
      pathEndOrSingleSlash {
        get {
          complete(TagService.find())
        } ~ post {
          entity(as[Tag]) { tag =>
            complete(StatusCodes.Created, TagService.add(tag))
          }
        }
      } ~ path(LongNumber) { id =>
        get {
          onSuccess(TagService.findById(id)) {
            case Some(tag) => complete(tag)
            case None => complete(StatusCodes.NotFound, "")
          }
        } ~ put {
          entity(as[Tag]) { tag =>
            onSuccess(TagService.update(tag)) {
              case 0 => complete(StatusCodes.Created, TagService.add(tag))
              case _ => onSuccess(TagService.findById(id)) {
                case Some(t) => complete(StatusCodes.OK, t)
                case None => complete(StatusCodes.InternalServerError, "Missing update target record")
              }
            }
          }
        } ~ delete {
          onSuccess(TagService.delete(id)) {
            case 0 => complete(StatusCodes.NotFound, "")
            case _ => complete(HttpEntity(ContentTypes.`application/json`, "{\"status\": \"ok\"}"))
          }
        }
      }
    }
}
