package com.github.ryu1okd.services

import com.github.ryu1okd.models.{Memo, MemoTags, Memos, Tag}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MemoTagService {

  def findByMemoId(memoId: Long): Future[(Option[Memo], Seq[Tag])] = {
    for {
      memo <- Memos find Some(memoId)
      memoTags <- MemoTags findTagsByMemoId Some(memoId)
    } yield (memo, memoTags.map(_._2))
  }

}
