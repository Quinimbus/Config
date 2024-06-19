package cloud.quinimbus.config;

import static org.junit.jupiter.api.Assertions.*;

import cloud.quinimbus.config.api.ConfigException;
import org.junit.jupiter.api.Test;

public class QuinimbusYAMLConfigTest {

    @Test
    public void test() throws ConfigException {
        var ctx = new ConfigContextImpl();
        assertEquals("A-B", ctx.asString("A", "B").orElseThrow());
    }
}
