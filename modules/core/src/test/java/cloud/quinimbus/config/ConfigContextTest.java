package cloud.quinimbus.config;

import static org.junit.jupiter.api.Assertions.*;

import cloud.quinimbus.common.annotations.Provider;
import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigProvider;
import java.io.Reader;
import java.io.StringReader;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class ConfigContextTest {

    @Provider(
            name = "YAML test config 1",
            alias = {},
            priority = 100)
    public static class YAMLConfig1 extends AbstractYAMLConfigProvider {

        @Override
        public Reader getSource() {
            return new StringReader(
                    """
                        A:
                            B:
                                C: "Test"
                        X:
                            XA: 13
                        """);
        }
    }

    @Provider(
            name = "YAML test config 2",
            alias = {},
            priority = 200)
    public static class YAMLConfig2 extends AbstractYAMLConfigProvider {

        @Override
        public Reader getSource() {
            return new StringReader(
                    """
                        X:
                            XA: "Hopefully not applied"
                            Y:
                                Z: "Test2"
                        """);
        }
    }

    public static record SLProviderImpl(Class<? extends ConfigProvider> type, ConfigProvider get)
            implements ServiceLoader.Provider<ConfigProvider> {}

    @Test
    public void testYAML() throws ConfigException {
        var ctx = new ConfigContextImpl(Stream.of(
                new SLProviderImpl(YAMLConfig1.class, new YAMLConfig1()),
                new SLProviderImpl(YAMLConfig2.class, new YAMLConfig2())));
        var test = ctx.asString("A", "B", "C");
        assertEquals("Test", test.orElseThrow());
        var empty = ctx.asString("A", "B", "D");
        assertTrue(empty.isEmpty());
        var testMerged = ctx.asString("X", "XA");
        assertEquals("13", testMerged.orElseThrow());
        var testMerged2 = ctx.asString("X", "Y", "Z");
        assertEquals("Test2", testMerged2.orElseThrow());
    }
}
