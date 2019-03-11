package io.github.yangziwen.cookieproxy;

import java.io.File;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ProxyConfig {

    static Config config = load();

    static String base_url = config.getString("base-url");

    static String cookie = config.getString("cookie");

    static Config load() {
        return ConfigFactory.parseFile(new File("conf/proxy.config"));
    }

}
