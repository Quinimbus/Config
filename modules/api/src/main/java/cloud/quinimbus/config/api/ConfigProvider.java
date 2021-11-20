package cloud.quinimbus.config.api;

import java.util.stream.Stream;

public interface ConfigProvider {
    
    Stream<? extends ConfigNode> provide() throws ConfigException;
}
