package cloud.quinimbus.config;

import cloud.quinimbus.config.api.ConfigNode;
import java.util.Optional;
import java.util.stream.Stream;

public class MergedConfigNode implements ConfigNode {

    private final ConfigNode node1;

    private final ConfigNode node2;

    public MergedConfigNode(ConfigNode node1, ConfigNode node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public String name() {
        return this.node1.name();
    }

    @Override
    public Stream<ConfigNode> stream() {
        return Stream.concat(this.node1.stream(), this.node2.stream());
    }

    @Override
    public String asString() {
        return this.node1.asString();
    }

    @Override
    public Optional<ConfigNode> asNode(int offset, String[] path) {
        var n1 = this.node1.asNode(offset, path);
        var n2 = this.node2.asNode(offset, path);
        if (n1.isEmpty()) {
            return n2;
        } else if (n2.isEmpty()) {
            return n1;
        } else {
            return Optional.of(new MergedConfigNode(n1.orElseThrow(), n2.orElseThrow()));
        }
    }

    @Override
    public Stream<String> asStringList() {
        return this.node1.asStringList();
    }
}
