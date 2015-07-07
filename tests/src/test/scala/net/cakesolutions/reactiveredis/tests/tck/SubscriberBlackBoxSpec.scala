package net.cakesolutions.reactiveredis.tests.tck

import akka.stream.scaladsl.{Sink, Source}
import net.cakesolutions.reactiveredis.driver.api.commands.{RedisRequest, Ping}
import org.reactivestreams.tck.{TestEnvironment, SubscriberBlackboxVerification}
import org.reactivestreams.{Publisher, Subscriber}
import org.scalatest.testng.TestNGSuiteLike

import scala.concurrent.duration.{FiniteDuration, _}
import scala.language.postfixOps

abstract class SubscriberBlackboxSpec(defaultTimeout: FiniteDuration)
  extends SubscriberBlackboxVerification[RedisRequest](new TestEnvironment(defaultTimeout.toMillis))
  with TestNGSuiteLike with TckBase {

  import TckBase._

  def this() = this(300 millis)

  override def createSubscriber(): Subscriber[RedisRequest] = {
    reactiveRedis(driver).subscriber()
  }

  def createHelperSource(elements: Long) : Source[RedisRequest, _] = elements match {
    case 0 => Source.empty
    case Long.MaxValue => Source(initialDelay = 10 millis, interval = 10 millis, tick = message)
    case n if n <= Int.MaxValue => Source(List.fill(n.toInt)(message))
    case n => sys.error("n > Int.MaxValue")
  }

  override def createHelperPublisher(elements: Long): Publisher[RedisRequest] = {
    createHelperSource(elements).runWith(Sink.publisher)
  }

  override def createElement(i: Int): RedisRequest = Ping
}