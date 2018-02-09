package com.github.ryu1okd.services

import com.github.ryu1okd.models.{Memo, Memos}

import scala.concurrent.Future

object MemoService {
  def delete(id: Long): Future[Int] = {
    Memos.delete(Some(id))
  }


  def update(memo: Memo): Future[Int] = {
    Memos.update(memo)
  }

  def findById(id: Long): Future[Option[Memo]] = {
    Memos.find(Some(id))
  }

  def add(memo: Memo): Future[Option[Memo]] = {
    for {
      insertedId <- Memos.create(memo)
      newMemo <- Memos.find(insertedId)
    } yield newMemo
  }

  def find(): Future[Seq[Memo]] = {
    Memos.findAll
  }

}
