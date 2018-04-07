package com.github.ryu1okd.protocols

import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue}
import DateTimeJsonProtocol._
import com.github.ryu1okd.models.{Memo, MemoTag, Tag}

trait MemoTagsProtocol extends DefaultJsonProtocol {

  def read(value: JsValue) = {
    MemoTag(Some(1), Some(1))
  }

  def write(memoTags: (Memo, Option[Seq[Tag]])): JsValue = {
    val memo: Memo = memoTags._1
    val maybeTags: Option[Seq[Tag]] = memoTags._2

    maybeTags match {
      case Some(tags) => JsObject(
        "id" -> JsNumber(memo.id.getOrElse(0l)),
        "body"-> JsString(memo.body),
        "tags" -> JsArray(tags.map(tag => JsObject(
          "id" -> JsNumber(tag.id.getOrElse(0l)),
          "name" -> JsString(tag.name)
        )).toVector))
      case None => JsObject(
        "id" -> JsNumber(memo.id.getOrElse(0l)),
        "body"-> JsString(memo.body),
        "tags" -> JsString("[]")
      )
    }
  }
}
