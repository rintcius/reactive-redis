package net.cakesolutions.reactiveredis.driver.japi;

import net.cakesolutions.reactiveredis.driver.api.results;
import net.cakesolutions.reactiveredis.driver.api.results.RedisResult;
import scala.Option;

import java.util.List;
import java.util.Optional;

public class Results {
    public static RedisResult ok() {
        return results.OK$.MODULE$;
    }

    public static RedisResult nok() {
        return results.NOK$.MODULE$;
    }

    public static <T> RedisResult result(T r) {
        return results.result(r);
    }

    public static RedisResult combinedResult(List<RedisResult> redisResults) {
        return results.jcombinedResult(redisResults);
    }

    public static <T> Optional<T> toJava(Option<T> option) {
        return option.isEmpty() ? Optional.<T>empty() : Optional.of(option.get());
    }
}
