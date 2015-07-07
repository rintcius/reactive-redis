package net.cakesolutions.reactiveredis.driver.rediscala

import akka.actor.ActorRefFactory
import akka.util.ByteString
import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.api.commands._
import net.cakesolutions.reactiveredis.driver.api.results._
import redis.{ByteStringDeserializer, RedisCommands, RedisClient}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class RediscalaDriver(redisClient: RedisClient)(implicit val executionContext: ExecutionContext) extends RedisDriver {

  override def onRequest(request: RedisRequest): Future[RedisResult] = request match {
    case c: RedisCommand => onCommand(redisClient, c)

    case Pipeline(commands) => {
      val pipeline = redisClient.pipeline
      val commandFutures = commands.map(onCommand(pipeline, _))
      pipeline.exec
      Future.sequence(commandFutures).map(CombinedResult)
    }
  }

  private def onCommand(commandContext: RedisCommands, command: RedisCommand): Future[RedisResult] = command match {
    case Dbsize => commandContext.dbsize.map(Result(_))
    case Flushall => commandContext.flushall.map(toStatusResult)
    case g:Get[_] => get(commandContext, g)
    case Ping => commandContext.ping.map(Result(_))
    case Set(k,v, ex, px, keyCheck) => commandContext.set(k, v, ex, px, keyCheck == KeyMustNotExist, keyCheck == KeyMustExist).map(toStatusResult)
  }

  private def get[R: ClassTag: ByteStringDeserializer](commandContext: RedisCommands, g: Get[R]): Future[Result[Option[R]]] =
    commandContext.get(g.key).map(Result(_))

  private def toStatusResult(b: Boolean): StatusResult = if (b) OK else NOK
}

object RediscalaDriver {
  def rediscalaDriver(host: String, port:Int)(implicit actorRefFactory: ActorRefFactory, executionContext: ExecutionContext) =
    new RediscalaDriver(redisClient(host, port))

  def redisClient(host: String, port:Int)(implicit actorRefFactory: ActorRefFactory) = new RedisClient(host, port)
}
