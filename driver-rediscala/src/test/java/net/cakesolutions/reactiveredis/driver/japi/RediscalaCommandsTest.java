package net.cakesolutions.reactiveredis.driver.japi;

import akka.actor.ActorSystem;
import net.cakesolutions.reactiveredis.driver.api.RedisDriver;
import net.cakesolutions.reactiveredis.driver.rediscala.Driver;
import redis.RedisClient;
import scala.Option;
import scala.concurrent.ExecutionContext;

public class RediscalaCommandsTest extends CommandsTest {

    @Override
    public RedisDriver redisDriver() {
        return new Driver(new RedisClient("localhost", 6379, Option.<String>empty(), Option.empty(), "redis", ActorSystem.apply()), ExecutionContext.Implicits$.MODULE$.global());
    }
}
