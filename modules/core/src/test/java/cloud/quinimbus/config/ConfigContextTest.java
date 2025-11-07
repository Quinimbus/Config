package cloud.quinimbus.config;

import static org.junit.jupiter.api.Assertions.*;

import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigProvider;
import cloud.quinimbus.tools.function.LazySingletonSupplier;
import java.io.Reader;
import java.io.StringReader;
import java.util.ServiceLoader;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class ConfigContextTest {

    public static class YAMLConfig1 extends AbstractYAMLConfigProvider {

        @Override
        public Reader getSource() {
            return new StringReader("""
                                    A:
                                        B:
                                            C: "Test"
                                    X:
                                        XA: 13
                                    """);
        }
    }

    public static class YAMLConfig2 extends AbstractYAMLConfigProvider {

        @Override
        public Reader getSource() {
            return new StringReader("""
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
        var impl = new TreeMap<String, LazySingletonSupplier<ConfigProvider>>();
        impl.put("yaml1", new LazySingletonSupplier<>(YAMLConfig1::new, YAMLConfig1.class));
        impl.put("yaml2", new LazySingletonSupplier<>(YAMLConfig2::new, YAMLConfig2.class));
        var ctx = new ConfigContextImpl(impl);
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
