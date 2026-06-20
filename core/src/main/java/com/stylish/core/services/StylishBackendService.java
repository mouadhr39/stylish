package com.stylish.core.services;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stylish.core.dto.Product;

import java.util.List;

public interface StylishBackendService {

    List<Product> getProducts();

    JsonObject getProductBySku(String sku);

    List<Product> getProductsByCategory(String code);

    JsonArray getCategories();

    JsonArray getCategoryById();

}
