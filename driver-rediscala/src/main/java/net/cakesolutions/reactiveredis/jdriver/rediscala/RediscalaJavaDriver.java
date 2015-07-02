package net.cakesolutions.reactiveredis.jdriver.rediscala;

import akka.actor.ActorRefFactory;
import net.cakesolutions.reactiveredis.driver.rediscala.RediscalaDriver;
import net.cakesolutions.reactiveredis.driver.rediscala.RediscalaDriver$;
import redis.RedisClient;
import scala.concurrent.ExecutionContext;

public class RediscalaJavaDriver extends RediscalaDriver {

    private final ExecutionContext executionContext;

    public RediscalaJavaDriver(RedisClient redisClient, ExecutionContext executionContext) {
        super(redisClient, executionContext);
        this.executionContext = executionContext;
    }

    public RediscalaJavaDriver(String host, int port, ActorRefFactory actorRefFactory, ExecutionContext executionContext) {
        super(createRedisClient(host, port, actorRefFactory), executionContext);
        this.executionContext = executionContext;
    }

    private static RedisClient createRedisClient(String host, int port, ActorRefFactory actorRefFactory) {
        return RediscalaDriver$.MODULE$.redisClient(host, port, actorRefFactory);
    }

    @Override
    public ExecutionContext executionContext() {
        return executionContext;
    }
}
