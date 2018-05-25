package com.github.ryu1okd.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.joda.time.DateTime
import slick.ast.ColumnOption.{AutoInc, PrimaryKey}
import slick.lifted
import slick.jdbc.MySQLProfile.api._
import com.github.tototoshi.slick.MySQLJodaSupport._
import com.github.ryu1okd.protocols.DateTimeJsonProtocol._
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

import scala.concurrent.Future

case class Tag (id:Option[Long], name:String, createdAt: Option[DateTime], updatedAt: Option[DateTime])

trait TagJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val tagFormat = jsonFormat4(Tag.apply)
}

class Tags(tag: lifted.Tag) extends Table[Tag] (tag, "tag") {
  def id = column[Option[Long]]("id", PrimaryKey, AutoInc)
  def name = column[String]("name")
  def createdAt = column[Option[DateTime]]("created_at")
  def updatedAt = column[Option[DateTime]]("updated_at")
  def * = (id, name, createdAt, updatedAt) <> (Tag.tupled, Tag.unapply)
}

object Tags extends TableQuery(new Tags(_)) {

  private val db = Database.forConfig("mysql")

  def create(tag: Tag): Future[Option[Long]] = {
    val date = new DateTime()
    val t = tag.copy(createdAt = Some(date), updatedAt = Some(date))
    db.run((this returning this.map(_.id)) += t)
  }

  def update(tag: Tag): Future[Int] = {
    val t = tag.copy(updatedAt = Some(new DateTime()))
    db.run(this.filter(_.id === tag.id).update(t))
  }

  def delete(id: Option[Long]): Future[Int] = {
    db.run(this.filter( _.id === id).delete)
  }

  def findAll():Future[Seq[Tag]] = {
    db.run(this.result)
  }

  def find(id: Option[Long]): Future[Option[Tag]] = {
    db.run(this.filter(_.id === id).result.headOption)
  }
}
