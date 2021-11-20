package cloud.quinimbus.config;

import cloud.quinimbus.common.annotations.Provider;
import cloud.quinimbus.config.api.ConfigContext;
import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigNode;
import cloud.quinimbus.config.api.ConfigProvider;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import name.falgout.jeffrey.throwing.stream.ThrowingStream;

public class ConfigContextImpl implements ConfigContext {

    private Map<String, ConfigNode> rootNodes;

    public ConfigContextImpl() throws ConfigException {
        this(ServiceLoader.load(ConfigProvider.class).stream());
    }

    // for unit testing
    ConfigContextImpl(Stream<java.util.ServiceLoader.Provider<ConfigProvider>> providers) throws ConfigException {
        this.rootNodes = ThrowingStream.of(providers, ConfigException.class)
                .peek(p -> {
                    System.out.println("Found config provider " + p.type());
                    if (p.type().getAnnotation(Provider.class) == null) {
                        throw new ConfigException(
                                "The provider class %s is missing the @Provider annotation".formatted(p.type().getName()));
                    }
                })
                .sorted((p1, p2) -> Integer.compare(
                        p1.type().getAnnotation(Provider.class).priority(),
                        p2.type().getAnnotation(Provider.class).priority()))
                .map(p -> p.get())
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
