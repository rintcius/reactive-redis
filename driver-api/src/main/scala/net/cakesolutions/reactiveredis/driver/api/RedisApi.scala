package net.cakesolutions.reactiveredis.driver.api

import net.cakesolutions.reactiveredis.driver.api.commands.RedisCommand
import net.cakesolutions.reactiveredis.driver.api.results.RedisResult

import scala.concurrent.Future

trait RedisDriver {
  def onCommand(command: RedisCommand): Future[RedisResult]
}

