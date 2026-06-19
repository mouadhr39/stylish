package com.stylish.core.services.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stylish.core.config.StylishBackEndConfig;
import com.stylish.core.services.StylishBackendService;

import com.stylish.core.utils.HttpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;


@Component(service = StylishBackendService.class, immediate = true)
@Designate(ocd = StylishBackEndConfig.class)

public class StylishBackendServiceImpl implements StylishBackendService {

    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    private HttpService httpService;

    private String getAllProductsEndpoint;
    private String getProductsByCategoryEndpoint;
    private String getProductBySkuEndpoint;
    private String getAllCategoriesEndpoint;
    private String getCategoryByIdEndpoint;

    @Activate
    @Modified
    protected void activate(StylishBackEndConfig config) {

        httpService = new HttpService(httpClientBuilderFactory)
                            .setConnectionTimeout(5000)
                            .setSocketTimeout(5000);

        getAllProductsEndpoint = config.domain() + config.getProductsEndpoint();
        getProductsByCategoryEndpoint = config.domain() + config.getProductByCategoryEndpoint();
        getProductBySkuEndpoint = config.domain() + config.getProductBySkuEndpoint();
        getCategoryByIdEndpoint = config.domain() + config.getCategoryByIdEndpoint();
        getAllCategoriesEndpoint = config.domain() + config.getCategoriesEndpoint();
    }


    @Override
    public JsonObject getProducts() {

        httpService.setEndpoint(getAllProductsEndpoint).build();
        httpService.execute();

        if (httpService.isStatusOk()) {
            JsonObject result =  httpService.getJsonObjectResponse();
            httpService.close();
            return result;
        }
        return new JsonObject();
    }

    @Override
    public JsonObject getProductsByCategory(String code) {

        httpService.setEndpoint(buildEndpoint(getProductsByCategoryEndpoint, "<ID>", code)).build();
        httpService.execute();

        if (httpService.isStatusOk()) {
            JsonObject result =  httpService.getJsonObjectResponse();
            httpService.close();
            return result;
        }
        return new JsonObject();
    }

    @Override
    public JsonObject getProductBySku(String sku) {

        httpService.setEndpoint(getProductBySkuEndpoint).build();
        httpService.execute();

        if (httpService.isStatusOk()) {
            JsonObject result =  httpService.getJsonObjectResponse();
            httpService.close();
            return result;
        }
        return new JsonObject();
    }

    @Override
    public JsonArray getCategories() {

        httpService.setEndpoint(getAllCategoriesEndpoint).build();
        httpService.execute();

        if (httpService.isStatusOk()) {
            JsonArray result =  httpService.getJsonArrayResponse();
            httpService.close();
            return result;
        }
        return new JsonArray();
    }

    @Override
    public JsonArray getCategoryById() {

        httpService.setEndpoint(getCategoryByIdEndpoint).build();
        httpService.execute();

        if (httpService.isStatusOk()) {
            JsonArray result =  httpService.getJsonArrayResponse();
            httpService.close();
            return result;
        }
        return new JsonArray();
    }

    private String buildEndpoint(String endpoint, String search, String target) {

        if (StringUtils.isNotBlank(endpoint)) {
           return  endpoint.replace(search, target);
        }
        return StringUtils.EMPTY;
    }
}
