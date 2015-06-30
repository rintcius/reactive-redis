package net.cakesolutions.reactiveredis.tests.tck

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.actor.WatermarkRequestStrategy
import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.api.commands.Ping
import net.cakesolutions.reactiveredis.driver.rediscala.Driver
import net.cakesolutions.reactiveredis.streams.ReactiveRedis

trait TckBase {

  def reactiveRedis(driver: RedisDriver) = new ReactiveRedis(driver, new WatermarkRequestStrategy(10))
  val driver: RedisDriver

  val message = Ping
}

object TckBase {
  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
}
