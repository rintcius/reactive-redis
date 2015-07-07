package net.cakesolutions.reactiveredis.tests.tck

package poc

import net.cakesolutions.reactiveredis.driver.api.commands.RedisRequest
import org.reactivestreams.tck.SubscriberWhiteboxVerification.{SubscriberPuppet, WhiteboxSubscriberProbe}
import org.reactivestreams.tck.{SubscriberWhiteboxVerification, TestEnvironment}
import org.reactivestreams.{Subscriber, Subscription}
import org.scalatest.testng.TestNGSuiteLike

import scala.concurrent.duration.{FiniteDuration, _}
import scala.language.postfixOps

abstract class SubscriberWhiteBoxSpec(defaultTimeout: FiniteDuration)
  extends SubscriberWhiteboxVerification[RedisRequest](new TestEnvironment(defaultTimeout.toMillis))
  with TestNGSuiteLike with TckBase {

  import TckBase._

  def this() = this(300 millis)

  override def createSubscriber(whiteboxSubscriberProbe: WhiteboxSubscriberProbe[RedisRequest]): Subscriber[RedisRequest] = {
    new SubscriberDecorator(reactiveRedis(driver).subscriber(), whiteboxSubscriberProbe)
  }

  override def createElement(i: Int) = message
}

class SubscriberDecorator[T](decoratee: Subscriber[T], probe: WhiteboxSubscriberProbe[T]) extends Subscriber[T] {

  override def onSubscribe(subscription: Subscription): Unit = {
    decoratee.onSubscribe(subscription)

    // register a successful Subscription, and create a Puppet,
    // for the WhiteboxVerification to be able to drive its tests:
    probe.registerOnSubscribe(new SubscriberPuppet() {
      override def triggerRequest(elements: Long) {
        subscription.request(elements)
      }

      override def signalCancel() {
        subscription.cancel()
      }
    })
  }

  override def onNext(t: T): Unit = {
    decoratee.onNext(t)
    probe.registerOnNext(t)
  }

  override def onError(throwable: Throwable): Unit = {
    decoratee.onError(throwable)
    probe.registerOnError(throwable)
  }

  override def onComplete(): Unit = {
    decoratee.onComplete()
    probe.registerOnComplete()
  }
}