package cloud.quinimbus.config.cdi;

import cloud.quinimbus.config.api.ConfigContext;
import cloud.quinimbus.config.api.ConfigException;
import cloud.quinimbus.config.api.ConfigNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConfigNodeProducer {

    @Inject
    private ConfigContext configContext;

    @Produces
    @Dependent
    public ConfigNode getConfigNode(InjectionPoint ip) throws ConfigException {
        var pathAnno = ip.getAnnotated().getAnnotation(ConfigPath.class);
        var path = pathAnno.value();
        if (pathAnno.optional()) {
            return this.configContext.asNode(path).orElse(null);
        } else {
            return this.configContext
                    .asNode(path)
                    .orElseThrow(() -> new ConfigException(
                            "Injected configuration for key %s is missing and not optional".formatted(path)));
        }
    }
}
