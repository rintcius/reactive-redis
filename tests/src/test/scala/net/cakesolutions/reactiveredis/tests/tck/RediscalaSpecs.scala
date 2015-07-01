package net.cakesolutions.reactiveredis.tests.tck

import net.cakesolutions.reactiveredis.driver.api.RedisDriver
import net.cakesolutions.reactiveredis.driver.rediscala.RediscalaDriver
import net.cakesolutions.reactiveredis.tests.tck.poc.SubscriberWhiteBoxSpec
import redis.RedisClient

object rediscalaCommon {

  import TckBase._

  val redisDriver = new RediscalaDriver(new RedisClient())
}

class RediscalaBlackBoxSpecs extends SubscriberBlackboxSpec {
  override val driver: RedisDriver = rediscalaCommon.redisDriver
}

class RediscalaWhiteBoxSpecs extends SubscriberWhiteBoxSpec {
  override val driver: RedisDriver = rediscalaCommon.redisDriver
}
