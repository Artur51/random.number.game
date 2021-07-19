package spring.project.random.number.play.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
public class EnvVariableResolveValueTest {

    @Autowired
    Environment env;
    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    private int minimumNumber;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    private int maximumNumber;

    @Test
    public void resolvePropertyTest() {
        String string1 = env.getProperty("properties.gameCalculation.minimumNumberInclusive");
        assertEquals("" + minimumNumber, string1);

        String string2 = env.resolveRequiredPlaceholders("${properties.gameCalculation.minimumNumberInclusive}");
        assertEquals("" + minimumNumber, string2);
    }
}