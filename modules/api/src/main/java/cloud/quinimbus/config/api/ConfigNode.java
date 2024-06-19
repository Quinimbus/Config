package cloud.quinimbus.config.api;

import java.util.stream.Stream;

public interface ConfigNode extends ConfigRoot {

    String name();

    Stream<? extends ConfigNode> stream();

    String asString();

    Stream<String> asStringList();
}
