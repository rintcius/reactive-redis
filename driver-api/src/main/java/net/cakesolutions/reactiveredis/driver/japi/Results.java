package net.cakesolutions.reactiveredis.driver.japi;

import net.cakesolutions.reactiveredis.driver.api.results;
import net.cakesolutions.reactiveredis.driver.api.results.RedisResult;

public class Results {
    public static RedisResult ok() {
        return results.OK$.MODULE$;
    }

    public static RedisResult nok() {
        return results.NOK$.MODULE$;
    }
}
