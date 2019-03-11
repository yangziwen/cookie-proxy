package io.github.yangziwen.cookieproxy.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import io.github.yangziwen.cookieproxy.ProxyConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import spark.Spark;

@Slf4j
public class ProxyController {

    private static final String HEADER_NAME_COOKIE = "Cookie";

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30,  TimeUnit.SECONDS)
            .build();

    private ProxyController() {}

    public static void init() {

        Spark.get("/*", (request, response) -> {
            return sendGetRequest(request, response);
        }, JSON::toJSONString);

    }

    private static Object sendGetRequest(spark.Request originRequest, spark.Response originResponse) {

        String url = ProxyConfig.base_url + "/" + StringUtils.join(originRequest.splat(), "/") + "?" + originRequest.queryString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader(HEADER_NAME_COOKIE, ProxyConfig.cookie)
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            for (String name : response.headers().names()) {
                originResponse.header(name, response.header(name));
            }
            return JSON.parseObject(response.body().string(), new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            String errorMsg = String.format("failed to send proxy request to url[{}]", url);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

}
