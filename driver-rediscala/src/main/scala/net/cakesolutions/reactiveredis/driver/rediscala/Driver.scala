package net.cakesolutions.reactiveredis.driver.rediscala

import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.api.commands._
import net.cakesolutions.reactiveredis.driver.api.results._
import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}

class Driver(redisClient: RedisClient)(implicit val executionContext: ExecutionContext) extends RedisDriver {

  override def onCommand(command: RedisCommand): Future[RedisResult] = command match {
    case Ping => redisClient.ping.map(StringResult)
    case Set(k,v, ex, px, nx, xx) => redisClient.set(k, v, ex, px, toBool(nx), toBool(xx)).map(toStatusResult)
  }

  private def toBool[A](option: Option[A]) = if (option.isDefined) true else false
  private def toStatusResult(b: Boolean): StatusResult = if (b) OK else NOK
}