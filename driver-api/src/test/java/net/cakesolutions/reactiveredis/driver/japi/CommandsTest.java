package net.cakesolutions.reactiveredis.driver.japi;

import net.cakesolutions.reactiveredis.driver.api.RedisDriver;
import net.cakesolutions.reactiveredis.driver.api.commands;
import net.cakesolutions.reactiveredis.driver.api.results;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.OptionalLong;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    public void testPing() throws Exception {
        testTemplate(
                Commands.ping(),
                Results.stringResult("PONG")
        );
    }

    @Test
    public void testSet() throws Exception {
        testTemplate(
                Commands.set(key, value),
                Results.ok()
        );
    }

    @Test
    public void testSetFullKeyMustNotExist() throws Exception {
        testTemplate(
                Commands.set(key, value, OptionalLong.of(1), OptionalLong.empty(), Commands.keyMustNotExist()),
                Results.ok()
        );
    }

    @Test
    public void testSetFullKeyMustExist() throws Exception {
        testTemplate(
                Commands.set(key, value, OptionalLong.empty(), OptionalLong.of(1), Commands.keyMustExist()),
                Results.nok()
        );
    }

    private void testTemplate(commands.RedisCommand command, results.RedisResult expected) throws Exception {
        final Future<results.RedisResult> future = redisDriver().onCommand(command);
        final results.RedisResult actual = Await.result(future, Duration.apply(1, TimeUnit.SECONDS));
        assertEquals(expected, actual);
    }
}