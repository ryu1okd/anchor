package com.github.ryu1okd.protocols

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.ISODateTimeFormat
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, deserializationError}

/**
  * joda.time.DateTimeをJSON変換するためのJsonProtocol
  */
object DateTimeJsonProtocol extends DefaultJsonProtocol {

  implicit object DateTimeJsonFormat extends RootJsonFormat[DateTime] {

    private lazy val format = ISODateTimeFormat.dateTimeNoMillis()

    def read(json: JsValue): DateTime = json match {
      case JsString(x) => format.parseDateTime(x)
      case x => deserializationError(s"Desiralization Error got ${x}")
    }

    def write(obj: DateTime): JsValue = JsString(format.print(obj.withZone(DateTimeZone.UTC)))
  }

}
