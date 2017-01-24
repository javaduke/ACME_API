package com.mulesoft.demo.util;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.util.IOUtils;

import java.util.HashMap;
import java.util.Map;

public class DynamicParseTemplateTransformer extends AbstractMessageTransformer {

    private Map<String, String> templates = new HashMap();

    private String location;

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding)
            throws TransformerException {
        try {
            String templateLocation = (String) muleContext
                    .getExpressionManager().parse(location, message);
            String template = (templates.containsKey(templateLocation) ? templates.get(templateLocation) : IOUtils.getResourceAsString(templateLocation, this.getClass()));
            templates.put(templateLocation, template);

            return muleContext.getExpressionManager().parse(template, message);
        } catch (Exception e) {
            throw new TransformerException(this, e);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
