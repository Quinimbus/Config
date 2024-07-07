import cloud.quinimbus.config.QuinimbusYAMLConfigProvider;
import cloud.quinimbus.config.api.ConfigProvider;

module cloud.quinimbus.config.core {
    provides ConfigProvider with
            QuinimbusYAMLConfigProvider;
    provides cloud.quinimbus.config.api.ConfigContext with
            cloud.quinimbus.config.ConfigContextImpl;

    uses ConfigProvider;

    requires cloud.quinimbus.common.annotations;
    requires cloud.quinimbus.config.api;
    requires cloud.quinimbus.tools;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires throwing.streams;
}
