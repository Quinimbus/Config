import cloud.quinimbus.config.api.ConfigProvider;
import cloud.quinimbus.config.microprofile.MicroprofileConfigProvider;

module cloud.quinimbus.config.microprofile {
    
    provides ConfigProvider with MicroprofileConfigProvider;
    
    requires cloud.quinimbus.common.annotations;
    requires cloud.quinimbus.config.api;
    requires microprofile.config.api;
}

