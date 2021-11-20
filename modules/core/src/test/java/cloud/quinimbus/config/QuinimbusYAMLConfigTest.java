package cloud.quinimbus.config;

import cloud.quinimbus.config.api.ConfigException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuinimbusYAMLConfigTest {
    
    @Test
    public void test() throws ConfigException {
        var ctx = new ConfigContextImpl();
        assertEquals("A-B", ctx.asString("A", "B").orElseThrow());
    }
}
