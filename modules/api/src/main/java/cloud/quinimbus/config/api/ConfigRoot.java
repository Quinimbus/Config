package cloud.quinimbus.config.api;

import java.util.Optional;
import java.util.stream.Stream;

public interface ConfigRoot {

    Optional<ConfigNode> asNode(int offset, String[] path);

    default Optional<ConfigNode> asNode(String... path) {
        return this.asNode(0, path);
    }

    default Optional<ConfigNode> asNode(String path) {
        return Optional.ofNullable(path).map(p -> p.split("\\.")).flatMap(this::asNode);
    }

    default Optional<String> asString(String... path) {
        return this.asNode(path).map(ConfigNode::asString);
    }

    default Optional<String> asString(String path) {
        return this.asNode(path).map(ConfigNode::asString);
    }

    default Stream<String> asStringList(String... path) {
        return this.asNode(path).map(ConfigNode::asStringList).orElseGet(Stream::empty);
    }

    default Stream<String> asStringList(String path) {
        return this.asNode(path).map(ConfigNode::asStringList).orElseGet(Stream::empty);
    }
}
