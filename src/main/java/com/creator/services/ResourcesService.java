package com.creator.services;

import com.creator.exceptions.ResourceNotFoundException;
import com.creator.models.DocumentData;
import com.creator.models.Resource;
import com.creator.models.ResourceType;
import com.creator.models.ResourceVersion;
import com.creator.repo.DocumentRepoR;
import com.creator.repo.ResourceRepo;
import com.creator.repo.ResourceTypeRepo;
import com.creator.repo.VersionRepo;
import com.creator.resource.IResource;
import com.creator.resource.ResourceLoaderService;
import com.creator.utils.ResourceTools;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class ResourcesService {

	private static final long SIZE = 1024l;

	@Autowired
	ResourceLoaderService resourceLoader;

	@Autowired
	ResourceRepo resourceRepo;

	@Autowired
	DocumentRepoR documentRepo;

	@Autowired
	VersionRepo versionRepo;

	@Autowired
	ResourceTypeRepo typesRepo;

	public Resource saveResource(Resource resource) {

		if(resource ==null)
			throw new IllegalArgumentException("resource can not be null");
		else if (resource.getType()==null)
			throw new IllegalArgumentException("resource type can not be null");

		if(resource.getType().getId() == null) {
			resource.setType(typesRepo.get(resource.getType().getName()));
		}

		Resource savedResource = resourceRepo.save(resource);
		ResourceVersion currentVersion = new ResourceVersion();
		ResourceVersion topVersion  = versionRepo.getResourceMaxVersion(savedResource.getId());
		if(topVersion!=null) {
			currentVersion.setName(topVersion.getNextVersionName());
		}

		currentVersion.setResource(savedResource);
		versionRepo.save(currentVersion);

		savedResource.setMaxVersion(currentVersion);
		resourceRepo.save(savedResource);

		JSONArray content = ResourceTools.toJsonArray(savedResource);
		this.writeResourceTofile(String.valueOf(savedResource.getId()), content.toString());

		return savedResource;

	}

	public List<ResourceVersion> getResourceVersion(Long resourceId){

		return versionRepo.getResourceVersions(resourceId);

	}

	public ResourceVersion getResourceMaxVersion(Long resourceId){

		return versionRepo.getResourceMaxVersion(resourceId);

	}

	public ResourceType saveType(ResourceType type)  {

		if(type!=null && type.hasName()) {

			return typesRepo.save(type);

		}else
			throw new IllegalArgumentException("Invalide Type");
	}

	public Resource get(Long id) {
		return  resourceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

	}

	public ResourceType getType(long id) {

		ResourceType type =  typesRepo.findById(id).orElse(null);

		if(type ==null) {
			throw new ResourceNotFoundException("ResourceType" ,id);
		}
		return type;
	}

	public List<ResourceType> getTypes() {

		return typesRepo.findAll();

	}

	public List<Resource> getResources() {
		return resourceRepo.findAll();
	}

	public ResourceType getType(String typeName) {
		return typesRepo.get(typeName);
	}

	public void writeResourceTofile(String fileName, String content)  {
		DocumentData doc = new DocumentData();
		doc.setId(fileName);
		doc.setContent(content);
		documentRepo.save(doc);
		IResource document = new  com.creator.resource.Resource(fileName,fileName, SIZE, new Date());
		resourceLoader.saveResource(document, content.getBytes());
	}

	public String readResourceFromFile(String fileName) {

		IResource resource= resourceLoader.getResource(fileName);
		String result = null;

		if(resource !=null)
			result= new String(resource.getContent());
		else
			throw new ResourceNotFoundException(fileName);

		return result;
	}

	@Transactional
	public void delete(long id) {

		try {
			versionRepo.deleteByResourceId(id);
			resourceRepo.deleteById(id);
			resourceLoader.deleteResource(String.valueOf(id));

		} catch (EmptyResultDataAccessException   e) {
			throw new ResourceNotFoundException(id);
		}
	}

	public String readResourceFromJsonFile(String id) {
		return readResourceFromFile(id.concat(".json"));
	}


	public String getResourcePath(String id) {

		try {
			return resourceLoader.getResource(id).toFile().getAbsolutePath();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

