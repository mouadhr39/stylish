package com.stylish.core.utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stylish.core.dto.Category;
import com.stylish.core.dto.Product;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class HttpService {

    private static final Logger LOG = LoggerFactory.getLogger(HttpService.class);
    private static final Gson gson = new Gson();
    private static final int DEFAULT_TIMEOUT = 2000;
    private final HttpClientBuilderFactory factory;

    private String endpoint;
    private int socketTimeout;
    private int connectionTimeout;

    private RequestConfig config;
    private HttpGet request;
    private CloseableHttpClient client;
    private CloseableHttpResponse response;

    private String result;
    private int status;

    public HttpService(HttpClientBuilderFactory httpClientBuilderFactory) {
        this.factory = httpClientBuilderFactory;
    }

    public HttpService setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        this.request = new HttpGet(endpoint);
        return this;
    }

    public HttpService setSocketTimeout(int timeout) {
        this.socketTimeout = timeout;
        return this;
    }

    public HttpService setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
        return this;
    }

    public void build() {

        initRequestConfig();

        client = factory.newBuilder().setDefaultRequestConfig(config).build();

    }

    public boolean execute() {

        try {
            response = client.execute(request);
            status = response.getStatusLine().getStatusCode();

            if (isStatusOk()) {
                result = response.getEntity() != null ? EntityUtils.toString(response.getEntity(), "UTF-8") : StringUtils.EMPTY;
            } else {
                LOG.warn("Http service connection failed. Status code: {}", status);
            }
            return true;
        } catch (IOException e) {
            LOG.error("Http service connection failed. IOException occurred while connecting to external endpoint: {}", endpoint, e);
            return false;
        }
    }

    public void close() {
        try {
            client.close();
            endpoint = null;
            result = null;
            response = null;
            status = 0;
        } catch (IOException e) {
            LOG.error("Http service connection failed. IOException occurred while closing connection", e);
        }

    }

    public JsonObject getJsonObjectResponse() {

        if (isStatusOk()) {
            return gson.fromJson(result, JsonObject.class);
        }
        return new JsonObject();
    }

    public JsonArray getJsonArrayResponse() {

        if (isStatusOk()) {
            return gson.fromJson(result, JsonArray.class);
        }
        return new JsonArray();
    }

    public <T> T getResponse(Class<T> type) {

        if (isStatusOk()) {
            try {
                return gson.fromJson(result, (Type) type);
            } catch (Exception e) {
                LOG.error("Http service failed. Exception occurred while parsing json string as Object: {}", result, e);
            }
        }
        return null;
    }

    public <T> List<T> getResponseList(Class<T> type) {

        if (isStatusOk()) {
            try {
                return gson.fromJson(result, (Type) type);
            } catch (Exception e) {
                LOG.error("Http service failed. Exception occurred while parsing json string as List: {}", result, e);
            }
        }
        return null;
    }

    public boolean isStatusOk() {
        return status == HttpStatus.SC_OK;
    }

    private void initRequestConfig() {

        if (socketTimeout > 0 && connectionTimeout > 0) {
            config = RequestConfig.custom()
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectionTimeout)
                    .build();
        } else {
            config = RequestConfig.custom()
                    .setSocketTimeout(DEFAULT_TIMEOUT)
                    .setConnectTimeout(DEFAULT_TIMEOUT)
                    .build();
        }

    }

}
