package com.github.ryu1okd.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.joda.time.DateTime
import slick.ast.ColumnOption.{AutoInc, PrimaryKey}
import slick.jdbc.MySQLProfile.api._
import spray.json._
import com.github.ryu1okd.protocols.DateTimeJsonProtocol._
import com.github.tototoshi.slick.MySQLJodaSupport._
import slick.lifted

import scala.concurrent.Future

case class Memo (id: Option[Long], body: String, createdAt: Option[DateTime], updatedAt: Option[DateTime])

trait MemoJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val memoFormat = jsonFormat4(Memo.apply)
}

class Memos(tag: lifted.Tag) extends Table[Memo] (tag, "memo") {
  def id = column[Option[Long]]("id", PrimaryKey, AutoInc)
  def body = column[String]("body")
  def createdAt = column[Option[DateTime]]("created_at")
  def updatedAt = column[Option[DateTime]]("updated_at")
  def * = (id, body, createdAt, updatedAt) <> (Memo.tupled, Memo.unapply)
}

object Memos extends TableQuery(new Memos(_)) {

  private val db = Database.forConfig("mysql")

  def create(memo: Memo): Future[Option[Long]] = {
    val date = new DateTime()
    val m = memo.copy(createdAt = Some(date), updatedAt = Some(date))
    db.run((this returning this.map(_.id)) += m)
  }

  def update(memo:Memo): Future[Int] = {
    val m = memo.copy(updatedAt = Some(new DateTime()))
    db.run(this.filter(_.id === memo.id).map(mq => (mq.body, mq.updatedAt)).update((m.body, m.updatedAt)))
  }

  def delete(id:Option[Long]): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }

  def findAll: Future[Seq[Memo]] = {
    db.run(this.result)
  }

  def find(id: Option[Long]): Future[Option[Memo]] = {
    db.run(this.filter(_.id === id).result.headOption)
  }

}
