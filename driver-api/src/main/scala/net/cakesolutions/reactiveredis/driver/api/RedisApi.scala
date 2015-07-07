package net.cakesolutions.reactiveredis.driver.api

import net.cakesolutions.reactiveredis.driver.api.commands.{RedisRequest, RedisCommand}
import net.cakesolutions.reactiveredis.driver.api.results.RedisResult

import scala.concurrent.Future

trait RedisDriver {
  def onRequest(request: RedisRequest): Future[RedisResult]
}

