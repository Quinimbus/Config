import cloud.quinimbus.config.api.ConfigProvider;
import cloud.quinimbus.config.QuinimbusYAMLConfigProvider;

module cloud.quinimbus.config.core {
    
    provides ConfigProvider with QuinimbusYAMLConfigProvider;
    
    uses ConfigProvider;
    
    requires cloud.quinimbus.common.annotations;
    requires cloud.quinimbus.config.api;
    requires cloud.quinimbus.tools;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires throwing.streams;
}
