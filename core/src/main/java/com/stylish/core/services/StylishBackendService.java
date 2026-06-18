package com.stylish.core.services;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface StylishBackendService {

    JsonObject getProducts();

    JsonObject getProductBySku(String sku);

    JsonObject getProductsByCategory(String code);

    JsonArray getCategories();

    JsonArray getCategoryById();

}
