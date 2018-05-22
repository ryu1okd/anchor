package com.github.ryu1okd.protocols

import com.github.ryu1okd.models.{Memo, Tag}
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonWriter, pimpAny}
import org.joda.time.DateTime
import DateTimeJsonProtocol._

trait MemoTagsProtocol extends DefaultJsonProtocol {

  implicit val memoTagsFormat:RootJsonWriter[(Memo, Seq[Tag])] = new RootJsonWriter[(Memo, Seq[Tag])] {

    override def write(memoTags: (Memo, Seq[Tag])): JsValue = {
      JsObject(
          "id" -> JsNumber(memoTags._1.id.getOrElse(0l)),
          "body" -> JsString(memoTags._1.body),
          "created_at" -> memoTags._1.createdAt.getOrElse(new DateTime).toJson,
          "updated_at" -> memoTags._1.updatedAt.getOrElse(new DateTime).toJson,
          "tags" -> JsArray(memoTags._2.map(tag => JsObject(
            "id" -> JsNumber(tag.id.getOrElse(0l)),
            "name" -> JsString(tag.name),
            "created_at" -> tag.createdAt.getOrElse(new DateTime).toJson,
            "updated_at" -> tag.updatedAt.getOrElse(new DateTime).toJson
          )).toVector))
    }
  }
}
