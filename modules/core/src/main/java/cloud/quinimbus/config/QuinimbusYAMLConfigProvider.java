package cloud.quinimbus.config;

import cloud.quinimbus.common.annotations.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

@Provider(
        name = "Quinimbus YAML Config Provider",
        alias = {"quinimbus.yml"},
        priority = 10)
public class QuinimbusYAMLConfigProvider extends AbstractYAMLConfigProvider {

    @Override
    public Reader getSource() {
        try (var is = this.locateYaml()) {
            if (is == null) {
                return new StringReader("");
            }
            return new InputStreamReader(new ByteArrayInputStream(is.readAllBytes()), Charset.forName("UTF-8"));
        } catch (IOException ex) {
            throw new IllegalStateException("Error while reading quinimbus.yml", ex);
        }
    }

    private InputStream locateYaml() throws IOException {
        var is = this.getClass().getResourceAsStream("/quinimbus.yml");
        if (is == null) {
            return ClassLoader.getSystemResourceAsStream("quinimbus.yml");
        }
        return is;
    }
}
