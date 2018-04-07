package com.github.ryu1okd.services

import com.github.ryu1okd.models.{Memo, MemoTags, Tag}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MemoTagService {

  def findByMemoId(memoId: Long): Future[Option[(Memo, Option[Seq[Tag]])]] = {
    MemoTags.findMemoAndTags(Some(memoId))
  }

}
