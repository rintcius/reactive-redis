package net.cakesolutions.reactiveredis.driver.japi;

import akka.actor.ActorSystem;
import net.cakesolutions.reactiveredis.driver.api.RedisDriver;
import net.cakesolutions.reactiveredis.driver.rediscala.RediscalaDriver$;
import scala.concurrent.ExecutionContext;

public class RediscalaCommandsTest extends CommandsTest {

    @Override
    public RedisDriver redisDriver() {
        return RediscalaDriver$.MODULE$.rediscalaDriver("localhost", 6379, ActorSystem.apply(), ExecutionContext.Implicits$.MODULE$.global());
    }
}
