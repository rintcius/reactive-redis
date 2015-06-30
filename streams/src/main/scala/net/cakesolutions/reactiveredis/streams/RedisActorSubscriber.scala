package net.cakesolutions.reactiveredis.streams

import akka.actor.ActorLogging
import akka.stream.actor.{RequestStrategy, ActorSubscriberMessage, ActorSubscriber}
import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.api.commands.RedisCommand

class RedisActorSubscriber(redisDriver: RedisDriver, override protected val requestStrategy: RequestStrategy)
  extends ActorSubscriber with ActorLogging {

  def receive = {
    case ActorSubscriberMessage.OnNext(elem) => onNext(elem)
    case ActorSubscriberMessage.OnError(ex) => onError(ex)
    case ActorSubscriberMessage.OnComplete => onComplete
  }

  private def onNext(elem: Any) = if (elem.isInstanceOf[RedisCommand])
    redisDriver.onCommand(elem.asInstanceOf[RedisCommand]) else
    onError(new RuntimeException("Unexpected command: " + elem))

  private def onError(ex: Throwable) = {
    log.error(ex, "Exception occurred in RedisActorSubscriber")
    onComplete
  }

  private def onComplete = {
    log.info("completing RedisActorSubscriber")
    context.stop(self)
  }
}
