package com.stylish.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "External Product API Configuration",
        description = "Configuration settings for fetching external product data"
)
public @interface StylishBackEndConfig {

    @AttributeDefinition(
            name = "API Domain",
            description = "The base domain of the external API (e.g., https://api.example.com)"
    )
    String domain() default "http://api.stylish.com";

    @AttributeDefinition(
            name = "Get Products Endpoint",
            description = "The endpoint path for products"
    )
    String getProductsEndpoint() default "/v1/products";

    @AttributeDefinition(
            name = "Get Product by SKU Endpoint",
            description = "The endpoint path for product by sku"
    )
    String getProductBySkuEndpoint() default "/v1/product/<SKU>";

    @AttributeDefinition(
            name = "Get Products by Category ID Endpoint",
            description = "The endpoint path for product by category"
    )
    String getProductByCategoryEndpoint() default "/v1/products/<ID>";


    @AttributeDefinition(
            name = "Get Categories Endpoint",
            description = "The endpoint path for categories"
    )
    String getCategoriesEndpoint() default "/v1/categories";

    @AttributeDefinition(
            name = "Get Categories Endpoint",
            description = "The endpoint path for category by id"
    )
    String getCategoryByIdEndpoint() default "/v1/category/<ID>";

}
