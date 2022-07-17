package com.creator.services;


import com.creator.models.Resource;
import com.creator.models.ResourceType;
import com.creator.resource.ResourceStorageService;
import com.creator.resource.ResourceStorageServiceProvider;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ResourcesService {

	ResourceStorageService storageService = ResourceStorageServiceProvider.getStorageService();

	public List<ResourceType> getTypes() {
		return null;
	}

	public void delete(long parseLong) {
	}

	public List<Resource> getResources() {
		return null;
	}

	public Resource saveResource(Resource resource) {
		return resource;
	}

	public ResourceType getType(Long id) {
		return null;
	}

	public Resource get(Long id) {
		return null;
	}
}

