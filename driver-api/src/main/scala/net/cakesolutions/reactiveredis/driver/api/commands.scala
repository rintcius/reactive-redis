package net.cakesolutions.reactiveredis.driver.api

object commands {
  sealed trait RedisCommand
  case object Ping extends RedisCommand

  trait KeyCheck
  case object KeyMustNotExist extends KeyCheck
  case object KeyMustExist extends KeyCheck
  case object NoKeyCheck extends KeyCheck


  case class Set(key: String, value: String, ex: Option[Long], px: Option[Long], keyCheck: KeyCheck) extends RedisCommand

  def ping = Ping

  def keyMustNotExist = KeyMustNotExist
  def keyMustExist = KeyMustExist
  def noKeyCheck = NoKeyCheck

  def set(key: String, value: String, ex: Option[Long], px: Option[Long], keyCheck: KeyCheck) = Set(key, value, ex, px, keyCheck)
  def set(key: String, value: String) = Set(key, value, None, None, noKeyCheck)

}
