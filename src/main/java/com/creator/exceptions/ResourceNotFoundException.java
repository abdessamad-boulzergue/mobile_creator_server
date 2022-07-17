package com.creator.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException(String entiyName, Long id) {
		
		super(entiyName.concat("  not found with id :").concat(String.valueOf(id)));
		
	}
	public ResourceNotFoundException(Long id) {
		super(" Entity not found, id :"+id);
	}

public ResourceNotFoundException(String  resource) {
	super(" resource not found :"+resource);
}
	
}
