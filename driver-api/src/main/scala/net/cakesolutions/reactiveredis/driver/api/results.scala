package net.cakesolutions.reactiveredis.driver.api

import scala.collection.JavaConverters._

object results {
  sealed trait RedisResult
  case class Result[T](r: T) extends RedisResult
  case class CombinedResult(results: List[RedisResult]) extends RedisResult

  sealed trait StatusResult extends RedisResult
  case object OK extends StatusResult
  case object NOK extends StatusResult

  def result[T](r: T) = Result(r)
  def combinedResult(results: List[RedisResult]) = CombinedResult(results)
  def jcombinedResult(results: java.util.List[RedisResult]) = combinedResult(results.asScala.toList)
}
