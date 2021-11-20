package cloud.quinimbus.config.cdi;

import cloud.quinimbus.config.api.ConfigContext;
import java.util.ServiceLoader;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped

public class ConfigContextProducer {

    private final ConfigContext configContext;

    public ConfigContextProducer() {
        this.configContext = ServiceLoader.load(ConfigContext.class).findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find any ConfigContext implementation"));
    }

    @Produces
    public ConfigContext getContext() {
        return this.configContext;
    }
}
