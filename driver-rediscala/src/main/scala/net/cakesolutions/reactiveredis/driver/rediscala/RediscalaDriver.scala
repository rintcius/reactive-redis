package net.cakesolutions.reactiveredis.driver.rediscala

import akka.actor.ActorRefFactory
import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.api.commands._
import net.cakesolutions.reactiveredis.driver.api.results._
import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}

class RediscalaDriver(redisClient: RedisClient)(implicit val executionContext: ExecutionContext) extends RedisDriver {

  override def onCommand(command: RedisCommand): Future[RedisResult] = command match {
    case Ping => redisClient.ping.map(StringResult)
    case Set(k,v, ex, px, keyCheck) => redisClient.set(k, v, ex, px, keyCheck == KeyMustNotExist, keyCheck == KeyMustExist).map(toStatusResult)
  }

  private def toBool[A](option: Option[A]) = if (option.isDefined) true else false
  private def toStatusResult(b: Boolean): StatusResult = if (b) OK else NOK
}

object RediscalaDriver {
  def rediscalaDriver(host: String, port:Int)(implicit actorRefFactory: ActorRefFactory, executionContext: ExecutionContext) =
    new RediscalaDriver(new RedisClient(host, port))
}
