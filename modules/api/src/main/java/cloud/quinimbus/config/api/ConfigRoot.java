package cloud.quinimbus.config.api;

import java.util.Optional;

public interface ConfigRoot {
        
    Optional<ConfigNode> asNode(int offset, String[] path);
        
    default Optional<ConfigNode> asNode(String... path) {
        return this.asNode(0, path);
    }
    
    default Optional<ConfigNode> asNode(String path) {
        return Optional.ofNullable(path)
                .map(p -> p.split("\\."))
                .flatMap(this::asNode);
    }
    
    default Optional<String> asString(String... path) {
        return this.asNode(path).map(ConfigNode::asString);
    }
    
    default Optional<String> asString(String path) {
        return this.asNode(path).map(ConfigNode::asString);
    }
}
