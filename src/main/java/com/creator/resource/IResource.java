package com.creator.resource;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface IResource extends Serializable {

    public static final String DOCUMENT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String PROPERTIES_SEPARATOR = "####";
    public static final String UPDATED_ATTRIBUTE = "_UPDATED_";

    void setIdentifier(String identifier);

    String getIdentifier();

    void setName(String name);

    String getName();

    void setSize(long size);

    long getSize();

    void setLastModified(Date lastModified);

    Date getLastModified();

    void setLink(String link);

    String getLink();

    String getAttribute(String attributeName);

    void addAttribute(String attributeName,
                      String attributeValue);

    void setAttributes(Map<String, String> attributes);

    Map<String, String> getAttributes();

    public void setContent(byte[] content);

    byte[] getContent();

    File toFile() throws IOException;
}