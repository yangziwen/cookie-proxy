package io.github.yangziwen.cookieproxy.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import io.github.yangziwen.cookieproxy.ProxyConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

        Spark.get("/*", ProxyController::sendGetRequest);

        Spark.post("/*", ProxyController::sendPostRequest);

        Spark.put("/*", ProxyController::sendPutRequest);

        Spark.delete("/*", ProxyController::sendDeleteRequest);

    }

    private static String sendGetRequest(spark.Request originRequest, spark.Response originResponse) {

        String queryString = StringUtils.isNotBlank(originRequest.queryString()) ? "?" + originRequest.queryString() : "";

        String url = ProxyConfig.base_url + "/" + StringUtils.join(originRequest.splat(), "/") + queryString;

        Request request = new Request.Builder()
                .url(url)
                .addHeader(HEADER_NAME_COOKIE, ProxyConfig.cookie)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            for (String name : response.headers().names()) {
                originResponse.header(name, response.header(name));
            }
            return response.body().string();
        } catch (IOException e) {
            String errorMsg = String.format("failed to send GET request to url[{}]", url);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private static String sendPostRequest(spark.Request originRequest, spark.Response originResponse) {

        String url = ProxyConfig.base_url + "/" + StringUtils.join(originRequest.splat(), "/");


        Request request = new Request.Builder()
                .url(url)
                .addHeader(HEADER_NAME_COOKIE, ProxyConfig.cookie)
                .post(RequestBody.create(MediaType.parse(originRequest.contentType()), originRequest.body()))
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            for (String name : response.headers().names()) {
                originResponse.header(name, response.header(name));
            }
            return response.body().string();
        } catch (IOException e) {
            String errorMsg = String.format("failed to send POST request to url[{}]", url);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }

    }

    private static String sendPutRequest(spark.Request originRequest, spark.Response originResponse) {

        String url = ProxyConfig.base_url + "/" + StringUtils.join(originRequest.splat(), "/");


        Request request = new Request.Builder()
                .url(url)
                .addHeader(HEADER_NAME_COOKIE, ProxyConfig.cookie)
                .put(RequestBody.create(MediaType.parse(originRequest.contentType()), originRequest.body()))
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            for (String name : response.headers().names()) {
                originResponse.header(name, response.header(name));
            }
            return response.body().string();
        } catch (IOException e) {
            String errorMsg = String.format("failed to send PUT request to url[{}]", url);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }

    }

    private static String sendDeleteRequest(spark.Request originRequest, spark.Response originResponse) {

        String url = ProxyConfig.base_url + "/" + StringUtils.join(originRequest.splat(), "/");

        Request request = new Request.Builder()
                .url(url)
                .addHeader(HEADER_NAME_COOKIE, ProxyConfig.cookie)
                .delete(RequestBody.create(MediaType.parse(originRequest.contentType()), originRequest.body()))
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            for (String name : response.headers().names()) {
                originResponse.header(name, response.header(name));
            }
            return response.body().string();
        } catch (IOException e) {
            String errorMsg = String.format("failed to send DELETE request to url[{}]", url);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

}
