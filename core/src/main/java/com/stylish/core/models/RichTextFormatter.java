package com.stylish.core.models;

import org.apache.commons.lang3.StringUtils;
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


    /**
     * NOTE: decorationType define how to use decoratorTag.
     *
     *  'append' OR 'replace'.
     *
     *  append: decoratorTag is used as container, none of the html tags will be replaced, instead they will be added as children of decoratorTag.
     *  replace: each <p>content</p> element will be replaced with the decoratorTag, if it is another tag the content will be appendend as text.
     *
     *  default behaviour is 'append'.
     */
    @Inject
    @Optional
    private String decorationType;

    @Inject
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

    private Element decoratorElement;


    @PostConstruct
    protected void init() {

        if (richText == null) {
            log.warn("Rich Text content is not provided. Rich text will be returned without decoration.");
            formattedHtml = richText;
            return;
        }

        if (decoratorTag == null) {
            log.warn("Decorator tag is not provided. Rich text will be returned without decoration.");
            formattedHtml = richText;
            return;
        }

        if (StringUtils.isEmpty(decorationType)) {
            decorationType = "append";
        }

        switch (decorationType) {
            case "append":
                decoratorElement = initDecoratorContainerElement();
                break;
            case "replace":
                initDecoratorContainerString();
        }

        Document document = org.jsoup.Jsoup.parse(richText);

        for (Element element : document.body().children()) {

            switch (decorationType) {
                case "append": {
                    decoratorElement.append(element.html());
                    decoratorElement.append("<br>");
                    break;
                }
                case "replace": {
                    if (element.is("p")) {
                        Element newElement = initDecoratorContainerElement();
                        newElement.html(element.html());
                        formattedHtml = String.format("%s%s", formattedHtml, newElement.outerHtml());
                    } else {
                        if(!element.text().isEmpty()) {
                            formattedHtml = String.format("%s%s", formattedHtml, element.text());
                        }
                    }
                    break;
                }
            }

        }

        if (decorationType.equals("append")) {
            formattedHtml = decoratorElement.outerHtml();
        }

    }


    private Element initDecoratorContainerElement() {
        Element decoratorElement = new Element(decoratorTag);

        if (decoratorId != null) {
            decoratorElement.attr("id", decoratorId);
        }

        if (decoratorClass != null) {
            decoratorElement.attr("class", decoratorClass);
        }

        if (decoratorStyle != null) {
            decoratorElement.attr("style", decoratorStyle);
        }

        return decoratorElement;
    }

    private void initDecoratorContainerString() {
        formattedHtml = "";
    }

    public String getRichText() {
        return richText;
    }

    public String getHtml() {
        return formattedHtml;
    }
}
