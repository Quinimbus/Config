package cloud.quinimbus.config;

import cloud.quinimbus.common.tools.ProviderLoader;
import cloud.quinimbus.config.api.ConfigContext;
import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigNode;
import cloud.quinimbus.config.api.ConfigProvider;
import cloud.quinimbus.tools.function.LazySingletonSupplier;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import name.falgout.jeffrey.throwing.stream.ThrowingStream;

public class ConfigContextImpl implements ConfigContext {

    private Map<String, ConfigNode> rootNodes;

    public ConfigContextImpl() throws ConfigException {
        this(ProviderLoader.loadProviders(ConfigProvider.class, ServiceLoader::load, false));
    }

    // for unit testing
    ConfigContextImpl(Map<String, LazySingletonSupplier<ConfigProvider>> providers) throws ConfigException {
        this.rootNodes = ThrowingStream.of(providers.entrySet().stream(), ConfigException.class)
                .peek(p -> System.out.println(
                        "Found config provider " + p.getValue().getRawType().getName()))
                .map(p -> p.getValue().get())
                .flatMap(cp -> ThrowingStream.of(cp.provide(), ConfigException.class))
                .collect(Collectors.toMap(cn -> cn.name(), cn -> cn, (cn1, cn2) -> new MergedConfigNode(cn1, cn2)));
    }

    @Override
    public Optional<ConfigNode> asNode(int offset, String... path) {
        if (offset != 0) {
            throw new IllegalArgumentException("offset has to be 0 when calling the ConfigContext directly");
        }
        return Optional.ofNullable(path)
                .filter(p -> p.length > 0)
                .map(p -> this.rootNodes.get(p[0]))
                .flatMap(p -> p.asNode(1, path));
    }
}
