package net.cakesolutions.reactiveredis.driver.api

object results {
  sealed trait RedisResult
  case class StringResult(s: String) extends RedisResult

  sealed trait StatusResult extends RedisResult
  case object OK extends StatusResult
  case object NOK extends StatusResult
}
