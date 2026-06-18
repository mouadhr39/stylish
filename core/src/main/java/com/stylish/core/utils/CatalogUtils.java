package com.stylish.core.utils;

import com.google.gson.JsonObject;
import com.stylish.core.dto.Category;
import com.stylish.core.dto.Product;

public class CatalogUtils {

    public static Product parseProductJson(JsonObject jsonObject) {
        return new Product();
    }

    public static Category parseCategoryJson(JsonObject jsonObject) {
        return new Category(0, "", "");
    }
}
