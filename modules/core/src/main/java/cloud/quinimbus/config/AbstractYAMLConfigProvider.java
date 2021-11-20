package cloud.quinimbus.config;

import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigNode;
import cloud.quinimbus.config.api.ConfigProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.Reader;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractYAMLConfigProvider implements ConfigProvider {

    public abstract Reader getSource();

    @Override
    public Stream<? extends ConfigNode> provide() throws ConfigException {
        var mapper = new ObjectMapper(new YAMLFactory());
        try ( var source = this.getSource()) {
            var root = mapper.readTree(source);
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(root.fields(), Spliterator.IMMUTABLE),
                    false)
                    .map(e -> new JsonNodeConfigNode(e.getKey(), e.getValue()));
        } catch (IOException ex) {
            throw new ConfigException("Cannot read the yaml configuration", ex);
        }
    }
}
