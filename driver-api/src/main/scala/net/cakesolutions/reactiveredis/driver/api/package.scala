package net.cakesolutions.reactiveredis.driver

import net.cakesolutions.reactiveredis.driver.api.commands.RedisCommand
import net.cakesolutions.reactiveredis.driver.api.results.RedisResult

import scala.concurrent.Future

package object api {
  trait RedisDriver {
    def onCommand(command: RedisCommand): Future[RedisResult]
  }
}
