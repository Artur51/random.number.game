package spring.project.random.number.play.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CalcUtils {

    public static boolean isFirstLess(Number value1, Number value2) {
        Objects.requireNonNull(value1);
        Objects.requireNonNull(value2);
        BigDecimal _value1 = asBigDecimal(value1);
        BigDecimal _value2 = asBigDecimal(value2);
        Objects.requireNonNull(_value1);
        Objects.requireNonNull(_value2);
        int result = _value1.compareTo(_value2);
        return result <= -1;
    }

    public static boolean isFirstBigger(Number value1, Number value2) {
        Objects.requireNonNull(value1);
        Objects.requireNonNull(value2);
        BigDecimal _value1 = asBigDecimal(value1);
        BigDecimal _value2 = asBigDecimal(value2);
        Objects.requireNonNull(_value1);
        Objects.requireNonNull(_value2);
        int result = _value1.compareTo(_value2);
        return result >= 1;
    }

    public static BigDecimal asBigDecimal(Number value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        if (value instanceof Integer
                || value instanceof Long
                || value instanceof Byte
                || value instanceof Short
                || value instanceof AtomicInteger
                || value instanceof AtomicLong
        ) {
            return BigDecimal.valueOf(value.longValue());
        }
        if (value instanceof Float || value instanceof Double) {
            return BigDecimal.valueOf(value.doubleValue());
        }
        Utils.throwRuntime("Not supported type " + value);
        return null;
    }

    public static int getRandom(
            Random random,
            int fromInclusive,
            int tillInclusive) {
        int bound = tillInclusive - fromInclusive;
        return random.nextInt(bound + 1) + fromInclusive;
    }
}
