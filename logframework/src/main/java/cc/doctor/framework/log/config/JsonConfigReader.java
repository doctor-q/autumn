package cc.doctor.framework.log.config;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonConfigReader implements ConfigReader {
    private static final String LOG_CONFIG_JSON = "log.json";
    private ConfigLoader configLoader = ConfigLoader.getSingleton();

    @Override
    public void load() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(URI.create(LOG_CONFIG_JSON)));
            String config = new String(bytes);
            configLoader.setConfig(JSON.parseObject(config));
        } catch (IOException e) {

        }
    }
}
