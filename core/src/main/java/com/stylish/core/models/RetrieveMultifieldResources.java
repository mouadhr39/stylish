package com.stylish.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class RetrieveMultifieldResources {

    @Inject
    private String path;

    @Inject
    private String node;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<Resource> resources;

    @PostConstruct
    protected void init() {

        resources = new LinkedList<>();

        if (StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(node)) {
            Resource parent = resourceResolver.getResource(path + "/" + node);

            if (parent != null) {

                for (Resource resource : parent.getChildren()) {
                    resources.add(resource);
                }
            }
        }
    }

    public List<Resource> getResources() {
        return resources;
    }

}
