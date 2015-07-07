package net.cakesolutions.reactiveredis.driver.api

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

object commands {
  sealed trait RedisRequest

  trait KeyCheck
  case object KeyMustNotExist extends KeyCheck
  case object KeyMustExist extends KeyCheck
  case object NoKeyCheck extends KeyCheck

  sealed trait RedisCommand extends RedisRequest
  case object Dbsize extends RedisCommand
  case object Flushall extends RedisCommand
  case class Get[R: ClassTag](key: String) extends RedisCommand
  case object Ping extends RedisCommand
  case class Set(key: String, value: String, ex: Option[Long], px: Option[Long], keyCheck: KeyCheck) extends RedisCommand

  case class Pipeline(commands: List[RedisCommand]) extends RedisRequest

  def keyMustNotExist = KeyMustNotExist
  def keyMustExist = KeyMustExist
  def noKeyCheck = NoKeyCheck

  def dbsize = Dbsize
  def flushall = Flushall
  def get[R: ClassTag](key: String) = Get[R](key)
  def ping = Ping
  def set(key: String, value: String, ex: Option[Long], px: Option[Long], keyCheck: KeyCheck) = Set(key, value, ex, px, keyCheck)
  def set(key: String, value: String) = Set(key, value, None, None, noKeyCheck)

  def pipeline(commands: List[RedisCommand]) = Pipeline(commands)
  def jpipeline(commands: java.util.List[RedisCommand]) = pipeline(commands.asScala.toList)
}
