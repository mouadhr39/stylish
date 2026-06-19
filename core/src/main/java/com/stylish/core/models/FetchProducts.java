package com.stylish.core.models;

import com.google.gson.JsonObject;
import com.stylish.core.dto.Product;
import com.stylish.core.services.StylishBackendService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class FetchProducts {

    private static final Logger LOG = LoggerFactory.getLogger(FetchProducts.class);

    @OSGiService
    private StylishBackendService service;

    @Inject
    @Optional
    private String categoryCode;

    @Inject
    @Optional
    private String[] productsSku;

    private List<Product> productList;

    @PostConstruct
    protected void init() {

        if (StringUtils.isNotEmpty(categoryCode)) {
            fetchProductsList(categoryCode);
        } else if (productsSku != null) {
            List<String> skus = Arrays.asList(productsSku);
            fetchProductsCollection(skus);
        } else {
            LOG.error("Fetch Products failed, neither CategoryCode nor ProductsSku has been configured.");
        }




    }

    private void fetchProductsCollection(List<String> skus) {

        for (String sku: skus) {
            service.getProductBySku(sku);
        }
    }

    private void fetchProductsList(String categoryCode) {
        JsonObject products = service.getProductsByCategory(categoryCode);


    }


}
