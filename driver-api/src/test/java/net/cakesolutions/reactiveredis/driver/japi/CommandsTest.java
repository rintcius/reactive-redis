package net.cakesolutions.reactiveredis.driver.japi;

import net.cakesolutions.reactiveredis.driver.api.RedisDriver;
import net.cakesolutions.reactiveredis.driver.api.commands;
import net.cakesolutions.reactiveredis.driver.api.results;
import org.junit.Before;
import org.junit.Test;
import scala.Option;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.OptionalLong;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static net.cakesolutions.reactiveredis.driver.japi.Results.ok;
import static org.junit.Assert.assertEquals;

public abstract class CommandsTest {

    public abstract RedisDriver redisDriver();

    private String key;
    private String value;

    @Before
    public void before() {
        key = UUID.randomUUID().toString();
        value = UUID.randomUUID().toString();
    }

    @Test
    public void testFlushall() throws Exception {
        testTemplate(
                Commands.flushall(),
                Results.ok()
        );
    }

    @Test
    public void testDbsize() throws Exception {
        testTemplate(
                Commands.flushall(),
                Results.ok()
        );
        testTemplate(
                Commands.dbsize(),
                Results.result(0)
        );
    }

    @Test
    public void testGet() throws Exception {
        testTemplate(
                Commands.set(key, value),
                ok()
        );
        testTemplate(
                Commands.get(key, String.class),
                Results.result(Option.apply(value))
        );
    }

    @Test
    public void testPing() throws Exception {
        testTemplate(
                Commands.ping(),
                Results.result("PONG")
        );
    }

    @Test
    public void testSet() throws Exception {
        testTemplate(
                Commands.set(key, value),
                ok()
        );
    }

    @Test
    public void testSetFullKeyMustNotExist() throws Exception {
        testTemplate(
                Commands.set(key, value, OptionalLong.of(1), OptionalLong.empty(), Commands.keyMustNotExist()),
                ok()
        );
    }

    @Test
    public void testSetFullKeyMustExist() throws Exception {
        testTemplate(
                Commands.set(key, value, OptionalLong.empty(), OptionalLong.of(1), Commands.keyMustExist()),
                Results.nok()
        );
    }

    @Test
    public void testPipeline() throws Exception {
        testTemplate(
                Commands.pipeline(
                        Arrays.asList(
                                Commands.set(key, value),
                                Commands.set(value, key)
                        )
                ),
                Results.combinedResult(Arrays.asList(ok(), ok()))
        );
    }

    private void testTemplate(commands.RedisRequest request, results.RedisResult expected) throws Exception {
        final Future<results.RedisResult> future = redisDriver().onRequest(request);
        final results.RedisResult actual = Await.result(future, Duration.apply(1, TimeUnit.SECONDS));
        assertEquals(expected, actual);
    }
}