package com.github.ryu1okd.protocols

import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonWriter}
import DateTimeJsonProtocol._
import com.github.ryu1okd.models.{Memo, MemoTag, Tag}

trait MemoTagsProtocol extends DefaultJsonProtocol {

  implicit val memoTagsFormat:RootJsonWriter[(Option[Memo], Seq[Tag])] = new RootJsonWriter[(Option[Memo], Seq[Tag])] {

    override def write(memoTags: (Option[Memo], Seq[Tag])): JsValue = {
      val maybeMemo: Option[Memo] = memoTags._1
      val tags: Seq[Tag] = memoTags._2

      maybeMemo match {
        case Some(memo) =>
          JsObject(
            "id" -> JsNumber(memo.id.getOrElse(0l)),
            "body" -> JsString(memo.body),
            "tags" -> JsArray(tags.map(tag => JsObject(
              "id" -> JsNumber(tag.id.getOrElse(0l)),
              "name" -> JsString(tag.name)
            )).toVector))
        case None => JsString("Memo notfound.")
      }
    }
  }
}
