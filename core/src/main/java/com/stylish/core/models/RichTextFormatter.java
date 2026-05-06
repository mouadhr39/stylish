package com.stylish.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class)
public class RichTextFormatter {

    private static final Logger log = LoggerFactory.getLogger(RichTextFormatter.class);

    @Inject
    private String richText;

    @Inject
    @Optional
    private String decoratorTag;

    @Inject
    @Optional
    private String decoratorId;

    @Inject
    @Optional
    private String decoratorClass;

    @Inject
    @Optional
    private String decoratorStyle;

    private String formattedHtml;


    @PostConstruct
    protected void init() {

        if (richText != null) {

            Document document = org.jsoup.Jsoup.parse(richText);

            for (Element p : document.select("p")) {
                log.info("Original p tag: " + p.outerHtml());
            }



        }

    }

    public String getRichText() {
        return richText;
    }

    public String getHtml() {
        return formattedHtml;
    }
}
