package cloud.quinimbus.config;

import cloud.quinimbus.config.api.ConfigNode;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonNodeConfigNode implements ConfigNode {

    private final String name;

    private final JsonNode node;

    public JsonNodeConfigNode(String name, JsonNode node) {
        this.name = name;
        this.node = node;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Stream<ConfigNode> stream() {
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(this.node.fields(), Spliterator.IMMUTABLE), false)
                .map(e -> new JsonNodeConfigNode(e.getKey(), e.getValue()));
    }

    @Override
    public String asString() {
        return this.node.asText();
    }

    @Override
    public Optional<ConfigNode> asNode(int offset, String... path) {
        if (offset == path.length) {
            return Optional.of(this);
        }
        return Optional.ofNullable(path)
                .filter(p -> p.length > offset)
                .map(p -> this.node.get(path[offset]))
                .flatMap(jn -> (new JsonNodeConfigNode(path[offset], jn)).asNode(offset + 1, path));
    }

    @Override
    public Stream<String> asStringList() {
        if (this.node.isArray()) {
            return StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(this.node.elements(), Spliterator.IMMUTABLE), false)
                    .map(JsonNode::asText);
        } else {
            return Stream.of(this.asString());
        }
    }
}
