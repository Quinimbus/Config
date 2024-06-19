package cloud.quinimbus.config.microprofile;

import cloud.quinimbus.config.api.ConfigNode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;

public class MicroprofileConfigNode implements ConfigNode {

    private final String name;

    private final ConfigValue mpValue;

    private final Config mpConfig;

    private final Map<String, MicroprofileConfigNode> children;

    public MicroprofileConfigNode(String name, ConfigValue mpValue, Config mpConfig) {
        this.name = name;
        this.mpValue = mpValue;
        this.children = new LinkedHashMap<>();
        this.mpConfig = mpConfig;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Stream<? extends ConfigNode> stream() {
        return this.children.values().stream();
    }

    @Override
    public String asString() {
        return this.mpValue.getValue();
    }

    @Override
    public Optional<ConfigNode> asNode(int offset, String[] path) {
        if (offset == path.length) {
            return Optional.of(this);
        }
        return Optional.ofNullable(path)
                .filter(p -> p.length > offset)
                .map(p -> this.children.get(path[offset]))
                .flatMap(cn -> cn.asNode(offset + 1, path));
    }

    public void addNode(int offset, String[] path, ConfigValue mpValue) {
        if (offset == path.length - 1) {
            this.children.put(path[offset], new MicroprofileConfigNode(path[offset], mpValue, this.mpConfig));
        } else {
            this.children
                    .computeIfAbsent(path[offset], k -> new MicroprofileConfigNode(k, null, this.mpConfig))
                    .addNode(offset + 1, path, mpValue);
        }
    }

    @Override
    public Stream<String> asStringList() {
        return this.mpConfig
                .getOptionalValues(this.mpValue.getName(), String.class)
                .map(List::stream)
                .orElseGet(Stream::empty);
    }
}
