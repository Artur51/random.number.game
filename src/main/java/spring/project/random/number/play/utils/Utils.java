package spring.project.random.number.play.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;

public class Utils {
    public static void throwRuntime(
            Throwable cause) {
        throw runtimeException("", cause);
    }

    public static void throwRuntime(
            String message) {
        throw runtimeException(message, null);
    }

    public static void throwRuntime(
            String message,
            Throwable cause) {
        throw runtimeException(message, cause);
    }

    public static void throwRuntime(
            Throwable cause,
            String message) {
        throw runtimeException(message, cause);
    }

    private static RuntimeException runtimeException(
            String message,
            Throwable cause) {
        return new RuntimeException(message, cause);
    }

    public static void shutdownAndWait(
            ExecutorService ex) {
        try {
            ex.shutdown();
            ex.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throwRuntime(e);
        }
    }

    public static void shutdownNowAndWait(
            ExecutorService ex) {
        try {
            ex.shutdownNow();
            ex.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throwRuntime(e);
        }
    }

    public static <T> void incrementValue(
            HashMap<T, Integer> map,
            T key) {
        Integer integer = map.get(key);
        if (integer == null) {
            map.put(key, Integer.valueOf(1));
        } else {
            map.put(key, integer + 1);
        }
    }

    public static List<String> collectErrors(
            BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return errors;
    }

    static Validator validator;// optimization. to long to get new.

    public static void validate(
            Object obj) {
        if (validator == null) {
            synchronized (Utils.class) {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                validator = factory.getValidator();
            }
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Object> cv : violations) {
                String message = cv.getMessage();
                throwRuntime("Validation Error " + message);
            }
        }
    }

    public static void notifyAll(
            Object lock) {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public static void wait(
            Object lock) {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void validate(
            boolean mustBeValid,
            String message) {
        if (!mustBeValid) {
            throwRuntime(message);
        }
    }

}
