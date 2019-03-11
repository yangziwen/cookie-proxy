package io.github.yangziwen.cookieproxy;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import io.github.yangziwen.cookieproxy.controller.ProxyController;
import spark.Spark;

public class Server {

    public static final String DEFAULT_HOST = "0.0.0.0";

    public static final int DEFAULT_PORT = 8041;

    private Server() {}

    public static void run(String ipAddress, int port) {

        port = ObjectUtils.defaultIfNull(port, DEFAULT_PORT);

        ipAddress = StringUtils.defaultString(ipAddress, DEFAULT_HOST);

        Spark.ipAddress(ipAddress);

        Spark.port(port);

        ProxyController.init();

        Runtime.getRuntime().addShutdownHook(new Thread(Spark::stop));

    }

}
