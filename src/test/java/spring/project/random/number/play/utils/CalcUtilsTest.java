package spring.project.random.number.play.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalcUtilsTest {

    @Test
    void isFirstLess() {
        assertFirstLess(Assertions::assertFalse, "10", (byte) 10);
        assertFirstLess(Assertions::assertFalse, "10", (byte) 9);

        assertFirstLess(Assertions::assertTrue, "19", (byte) 20);
        assertFirstLess(Assertions::assertTrue, "20.9999999999999999999999999999999999999999999999999", (byte) 21);

        assertFirstLess(Assertions::assertFalse, "10", 10f);
        assertFirstLess(Assertions::assertFalse, "10", 9f);

        assertFirstLess(Assertions::assertTrue, "19", 20f);
        assertFirstLess(Assertions::assertTrue, "20.9999999999999999999999999999999999999999999999999", 21f);
    }

    @Test
    void isFirstBigger() {
        assertFirstBigger(Assertions::assertFalse, "10", (byte) 10);
        assertFirstBigger(Assertions::assertFalse, "10", (byte) 20);

        assertFirstBigger(Assertions::assertTrue, "100", (byte) 20);
        assertFirstBigger(Assertions::assertTrue, "20.000000000000000000000000000000001", (byte) 20);

        assertFirstBigger(Assertions::assertFalse, "10", 10f);
        assertFirstBigger(Assertions::assertFalse, "10", 20f);

        assertFirstBigger(Assertions::assertTrue, "100", 20f);
        assertFirstBigger(Assertions::assertTrue, "20.000000000000000000000000000000001", 20f);
    }

    public void assertFirstBigger(
            BooleanBiConsumer<String> call,
            String bigDecimal,
            byte num) {
        BigDecimal value1 = new BigDecimal(bigDecimal);
        for (Number n : getNumericInstances(num)) {
            call.accept(CalcUtils.isFirstBigger(value1, n), "isFirstBiggerForNumericValues method Failed for value " + n);
        }
    }

    public void assertFirstLess(
            BooleanBiConsumer<String> call,
            String bigDecimal,
            byte num) {
        BigDecimal value1 = new BigDecimal(bigDecimal);
        for (Number n : getNumericInstances(num)) {
            call.accept(CalcUtils.isFirstLess(value1, n), "isFirstLessForNumericValues method Failed for value " + n);
        }
    }

    public void assertFirstBigger(
            BooleanBiConsumer<String> call,
            String bigDecimal,
            float num) {
        BigDecimal value1 = new BigDecimal(bigDecimal);
        for (Number n : getFloatingInstances(num)) {
            call.accept(CalcUtils.isFirstBigger(value1, n), "isFirstBiggerForNumericValues method Failed for value " + n);
        }
    }

    public void assertFirstLess(
            BooleanBiConsumer<String> call,
            String bigDecimal,
            float num) {
        BigDecimal value1 = new BigDecimal(bigDecimal);
        for (Number n : getFloatingInstances(num)) {
            call.accept(CalcUtils.isFirstLess(value1, n), "isFirstLessForNumericValues method Failed for value " + n);
        }
    }

    public interface BooleanBiConsumer<U> {
        void accept(
                boolean b,
                U u);
    }

    private static Number[] getNumericInstances(
            byte value) {
        return new Number[] { Byte.valueOf(value), //
                Short.valueOf(value), //
                Integer.valueOf(value), //
                Long.valueOf(value), //
                new AtomicInteger(value), //
                new AtomicLong(value), //
                BigInteger.valueOf(value)//
        };
    }

    private static Number[] getFloatingInstances(
            float value) {
        return new Number[] { //
                Float.valueOf(value), //
                Double.valueOf(value),//
        };
    }

    @Test
    void testRandom() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int randomValue = CalcUtils.getRandom(random, 0, 10);
            assertTrue(randomValue >= 0, "Minimu is wrong " + randomValue);
            assertTrue(randomValue <= 10, "Maximus is wrong " + randomValue);
        }
    }

    @Test
    void testRandomCheckAllValuesGenerated() {
        Random random = new Random();
        int min = 0;
        int max = 15;
        Set<Integer> set = IntStream.range(min, max).boxed().collect(Collectors.toSet());
        assertTrue(!set.isEmpty(), "Must not be empty");
        for (int i = 0; i < 100000; i++) {
            int randomValue = CalcUtils.getRandom(random, min, max);
            set.remove(randomValue);
            if (set.isEmpty()) {
                return;
            }
        }
        assertTrue(false, "Cannot validate that all test values are generated into checker set; ungenerated are: " + set);
    }

}