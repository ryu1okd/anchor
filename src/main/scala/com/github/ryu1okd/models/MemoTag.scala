package com.github.ryu1okd.models

import org.joda.time.DateTime
import spray.json._
import com.github.ryu1okd.protocols.DateTimeJsonProtocol._
import slick.jdbc.MySQLProfile.api._
import spray.json._
import com.github.tototoshi.slick.MySQLJodaSupport._
import slick.lifted

import scala.concurrent.Future

case class MemoTag (memoId: Option[Long], tagId: Option[Long])

class MemoTags(tag: lifted.Tag) extends Table[MemoTag] (tag, "memo_tag") {
  def memoId = column[Option[Long]]("memo_id")
  def tagId = column[Option[Long]]("tag_id")
  def * = (memoId, tagId) <> (MemoTag.tupled, MemoTag.unapply)
  def pk = primaryKey("primaryKey" , (memoId, tagId))

  def memoFk = foreignKey("FK_MEMO", memoId, TableQuery[Memos])(memo => memo.id, onDelete = ForeignKeyAction.Cascade)
  def tagFk = foreignKey("FK_TAG", tagId, TableQuery[Tags])(tag => tag.id, onDelete = ForeignKeyAction.Cascade)

}

object MemoTags extends TableQuery(new MemoTags(_)) {

  val tags = TableQuery[Tags]

  private val db = Database.forConfig("mysql")

  def findTagsByMemoId(memoId: Option[Long]): Future[Seq[(MemoTag, Tag)]] = {
    val q = this.join(tags).on(_.tagId === _.id).filter{ case (mts, ts) => mts.memoId === memoId}
    db.run(q.result)
  }
}
