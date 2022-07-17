package com.creator.resource;


import com.creator.utils.LazyLoaded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "resource-object")
public class Resource implements IResource, LazyLoaded<ResourceStorageService> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1349595244757800376L;

	private static final Logger logger = LoggerFactory.getLogger(Resource.class);
    
    private String identifier = "";
    private String name;
    private byte[] content;
    private Map<String, String> attributes;

    private String link = "";
    private long size;
    private Date lastModified;
    
    private transient ResourceStorageService service;

    public Resource() {
        // Setting default values for identifier && download link
        this.identifier = UUID.randomUUID().toString();
        this.attributes = new HashMap<String, String>();
    }

    public Resource(String identifier,
                    String name,
                    long size,
                    Date lastModified) {
        this.identifier = identifier;
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        attributes = new HashMap<String, String>();
    }

    public Resource(String name,
                    long size,
                    Date lastModified) {
        this();
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        attributes = new HashMap<String, String>();
    }

    public Resource(String identifier,
                    String name,
                    long size,
                    Date lastModified,
                    String link) {
        this.identifier = identifier;
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        this.link = link;
        attributes = new HashMap<String, String>();
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getLink() {
        return this.link;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public String getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    @Override
    public void addAttribute(String attributeName,
                             String attributeValue) {
        attributes.put(attributeName,
                       attributeValue);
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
        addAttribute(UPDATED_ATTRIBUTE, "true");
    }

    @Override
    public byte[] getContent() {
        load();
        
        return content;
    }

    @Override
    public File toFile() throws IOException {
        String[] nameParts = getName().split("\\.");
        File file = File.createTempFile(nameParts[0], "." + nameParts[1]);
        file.setLastModified(getLastModified().getTime());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getContent());
        fos.close();
        return file;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DOCUMENT_DATE_PATTERN);
        return name + PROPERTIES_SEPARATOR + size + PROPERTIES_SEPARATOR + ((lastModified != null) ? sdf.format(lastModified) : "") + PROPERTIES_SEPARATOR + identifier;
    }

    /*
     * lazy load support
     */
    
    @Override
    public void setLoadService(ResourceStorageService service) {
        this.service = service;
    }

    @Override
    public void load() {
        if (content == null && service != null && identifier != null) {
            content = service.loadContent(identifier);
        } else {
            logger.debug("Cannot load content due to missing service {} or identifier {}", service, identifier);
        }
    }
}
