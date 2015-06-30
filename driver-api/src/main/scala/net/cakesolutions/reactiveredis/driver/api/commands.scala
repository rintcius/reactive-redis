package net.cakesolutions.reactiveredis.driver.api

object commands {
  sealed trait RedisCommand
  case object Ping extends RedisCommand

  case object NX
  case object XX

  case class Set(key: String, value: String, ex: Option[Long], px: Option[Long], nx: Option[NX.type], xx: Option[XX.type]) extends RedisCommand

}
