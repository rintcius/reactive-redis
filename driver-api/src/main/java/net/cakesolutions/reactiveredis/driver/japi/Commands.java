package net.cakesolutions.reactiveredis.driver.japi;

import net.cakesolutions.reactiveredis.driver.api.commands;
import net.cakesolutions.reactiveredis.driver.api.commands.RedisCommand;
import scala.Option;

import java.util.Optional;
import java.util.OptionalLong;

public class Commands {
    public static commands.KeyCheck keyMustNotExist() {
        return commands.keyMustNotExist();
    }

    public static commands.KeyCheck keyMustExist() {
        return commands.keyMustExist();
    }

    public static commands.KeyCheck noKeyCheck() {
        return commands.noKeyCheck();
    }

    public static RedisCommand ping() {
        return commands.ping();
    }

    public static RedisCommand set(String key, String value) {
        return commands.set(key, value);
    }

    public static RedisCommand set(String key, String value, OptionalLong ex, OptionalLong px, commands.KeyCheck keyCheck) {
        return commands.set(key, value, toScalaLong(ex), toScalaLong(px), keyCheck);
    }

    private static Option<Object> toScalaLong(OptionalLong optional) {
        return optional.isPresent() ? Option.apply(optional.getAsLong()) : Option.empty();
    }

    private static <T> Option<T> toScala(Optional<T> optional) {
        return optional.isPresent() ? Option.apply(optional.get()) : Option.empty();
    }
}
