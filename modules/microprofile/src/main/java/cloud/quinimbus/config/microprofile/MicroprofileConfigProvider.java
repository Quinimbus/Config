package cloud.quinimbus.config.microprofile;

import cloud.quinimbus.common.annotations.Provider;
import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigNode;
import cloud.quinimbus.config.api.ConfigProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.eclipse.microprofile.config.ConfigValue;

@Provider(name = "Microprofile configuration provider", alias = "microprofile", priority = 100)
public class MicroprofileConfigProvider implements ConfigProvider {

    private final Map<String, MicroprofileConfigNode> rootNodes;

    public MicroprofileConfigProvider() {
        this.rootNodes = new LinkedHashMap<>();
    }

    @Override
    public Stream<? extends ConfigNode> provide() throws ConfigException {
        var mpConfig = org.eclipse.microprofile.config.ConfigProvider.getConfig();
        StreamSupport.stream(mpConfig.getPropertyNames().spliterator(), false)
                .filter(k -> k.startsWith("quinimbus."))
                .map(mpConfig::getConfigValue)
                .sorted((cv1, cv2) -> cv1.getName().compareTo(cv2.getName()))
                .forEach(this::addToTree);
        return this.rootNodes.values().stream();
    }

    private void addToTree(ConfigValue mpValue) {
        var path = mpValue.getName().substring(10).split("\\.");
        if (path.length == 1) {
            this.rootNodes.put(path[0], new MicroprofileConfigNode(path[0], mpValue));
        } else {
            this.rootNodes.computeIfAbsent(
                    path[0],
                    k -> new MicroprofileConfigNode(path[0], mpValue)).addNode(1, path, mpValue);
        }
    }
}
