package com.creator.resource;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Simple storage service definition
 */
public interface ResourceStorageService {

    /**
     * Generates a Resource instance.
     * @param name          The document name
     * @param size          The document size
     * @param lastModified  The lastModified date of the document
     * @param params    A Map<String, String> containing the params to create the document.
     * @return
     */
	IResource buildResource( String name, long size, Date lastModified, Map<String, String> params );

    /**
     * Method to store the uploaded file on the system
     * @param document      The document to store the content
     * @param content       The document content
     * @return              A Resource
     */
    IResource saveResource(IResource document, byte[] content);

    /**
     * Method to obtain a File for the given storage id
     * @param id            The Resource id to obtain the Resource
     * @return              The java.io.File identified with the id
     */
    IResource getResource(String id);
    
    /**
     * Loads document content 
     * @param id unique id of the document
     * @return loaded document's content
     */
    byte[] loadContent(String id);

    /**
     * Deletes the File identified by the given id
     * @param id            The Resource id to delete
     * @return              true if it was possible to remove, false if not
     */
    boolean deleteResource(String id);

    /**
     * Deletes the File identified by the given id
     * @param document      The Resource to delete
     * @return              true if it was possible to remove, false if not
     */
    boolean deleteResource(IResource document);
    
    /**
     * Lists available document with paging support.
     * @param page page to be displayed
     * @param pageSize number of elements to return
     * @return
     */
    List<IResource> listResources(Integer page, Integer pageSize);
}
