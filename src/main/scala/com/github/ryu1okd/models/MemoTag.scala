package com.github.ryu1okd.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import slick.jdbc.MySQLProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType
import slick.lifted
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, PrettyPrinter}

import scala.concurrent.Future

case class MemoTag (memoId: Option[Long], tagId: Option[Long])

class MemoTags(tag: lifted.Tag) extends Table[MemoTag] (tag, "memoTag") {
  def memoId = column[Option[Long]]("memo_id")
  def tagId = column[Option[Long]]("tag_id")
  def * = (memoId, tagId) <> (MemoTag.tupled, MemoTag.unapply)
  def pk = primaryKey("primaryKey" , (memoId, tagId))

  def memoFk = foreignKey("FK_MEMO", memoId, TableQuery[Memos])(memo => memo.id, onDelete = ForeignKeyAction.Cascade)
  def tagFk = foreignKey("FK_TAG", tagId, TableQuery[Tags])(tag => tag.id, onDelete = ForeignKeyAction.Cascade)

}

object MemoTags extends TableQuery(new MemoTags(_)) {

  private val db = Database.forConfig("mysql")

  private val memos = TableQuery[Memos]
  private val tags = TableQuery[Tags]

  def findMemoAndTags(memoId: Option[Long]): Future[Option[(Memo, Option[Seq[Tag]])]] = {
//    val q = for{
//      m <- memos.filter(_.id === memoId)
//      mt <- this if mt.memoId === m.id
//      ts <- tags if mt.tagId === ts.id
//    } yield (m, ts)

    val q = for {
      ((m, mts), ts) <- memos joinLeft this on (_.id === _.memoId) join tags on (_._2.tagId === _.id)
      if memos.filter( _.id === memoId)
    } yield (m, ts)

    db.run(q.result)

  }
}
