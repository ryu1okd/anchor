package com.github.ryu1okd.services

import com.github.ryu1okd.models.{Tag, Tags}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TagService {

  def add(tag: Tag): Future[Option[Tag]] = {
    for {
      insertedId <- Tags.create(tag)
      newTag <- Tags.find(insertedId)
    } yield newTag
  }

  def find(): Future[Seq[Tag]] = {
    Tags.findAll()
  }

  def delete(id: Long): Future[Int] = {
    Tags.delete(Some(id))
  }

  def update(tag: Tag): Future[Int] = {
    Tags.update(tag)
  }

  def findById(id: Long): Future[Option[Tag]] = {
    Tags.find(Some(id))
  }
}
