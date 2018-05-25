CREATE DATABASE anchor DEFAULT CHARSET utf8mb4;

CREATE TABLE anchor.memo (
  id bigint auto_increment NOT NULL PRIMARY KEY ,
  body TEXT,
  created_at DATETIME,
  updated_at DATETIME
);

CREATE TABLE anchor.tag (
  id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name TEXT NOT NULL,
  created_at DATETIME,
  updated_at DATETIME
);

CREATE TABLE anchor.memo_tag (
  memo_id bigint,
  tag_id bigint
);
