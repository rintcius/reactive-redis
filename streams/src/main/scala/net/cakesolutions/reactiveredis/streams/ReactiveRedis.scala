package net.cakesolutions.reactiveredis.streams

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.actor.{RequestStrategy, ActorSubscriber}
import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.api.commands.RedisCommand
import org.reactivestreams.Subscriber

class ReactiveRedis(driver: RedisDriver, requestStrategy: RequestStrategy) {

  def subscriber()(implicit actorSystem: ActorSystem): Subscriber[RedisCommand] = {
    ActorSubscriber[RedisCommand](subscriberActor)
  }

  def subscriberActor(implicit actorSystem: ActorSystem): ActorRef = {
    actorSystem.actorOf(Props(new RedisActorSubscriber(driver, requestStrategy)).withDispatcher("redis-subscriber-dispatcher"))
  }
}
