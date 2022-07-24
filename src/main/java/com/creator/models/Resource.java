package com.creator.models;

import com.creator.utils.ResourceTools;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="resource")
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 3)
	private String name;
	
	private String description;

	@OneToOne
	@JoinColumn(name="max_version_id",referencedColumnName = "id")
	private ResourceVersion maxVersion;

	@OneToMany(mappedBy = "resource",fetch = FetchType.EAGER)
	private List<ResourceVersion> versions;


	@ManyToOne
	@JoinColumn(name="type_id")
	@NotNull
	private ResourceType type ;



	public Long getId() {
		return id;
	}



	public String getName() {
		return name;
	}



	public String getDescription() {
		return description;
	}



	public List<ResourceVersion> getVersions() {
		return versions;
	}



	public void setVersions(List versions) {
		this.versions = versions;
	}

	public ResourceType getType() {
		return type;
	}


	public void setType(ResourceType type) {
		this.type = type;
	}

	public ResourceVersion getMaxVersion() {
		return maxVersion;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMaxVersion(ResourceVersion maxVersion) {
		this.maxVersion = maxVersion;
	}



	public static Resource from(JSONObject attributes) {
		Long id = Long.parseLong(attributes.get(ResourceTools.ATTR_RESDESC_ID).toString());
		String name = attributes.getString(ResourceTools.ATTR_NAME);
		Resource res = new Resource();
		res.setName(name);
		res.setId(id);
		return res;
	}

	public static ResourceType getResourceType(String type) {
		ResourceType restType =  new ResourceType();
		restType.setName(type);
		return restType;
	}

	private void setId(Long id) {
	   this.id = id;
	}

	public static Resource getWorkflowResource() {
		Resource resource = new Resource();
		resource.setName("New Workflow");
		resource.setDescription("workflow");
		resource.setType(Resource.getResourceType(ResourceType.TYPE_WORKFLOW));
		return resource;
	}
	
	public static Resource getDocumentResource() {
		Resource resource = new Resource();
		resource.setName("New Document");
		resource.setDescription("document editor");
		resource.setType(Resource.getResourceType(ResourceType.TYPE_DOCUMENT));
		return resource;
	}

	public static Resource getResource(String type) {
		Resource resource = new Resource();
		resource.setName("New "+type);
		resource.setDescription(" new resource of type "+type);
		resource.setType(Resource.getResourceType(type));
		return resource;
	}

	public static Resource getApplicationResource() {
		Resource resource = new Resource();
		resource.setName("New Application");
		resource.setDescription("mobile application");
		resource.setType(Resource.getResourceType(ResourceType.TYPE_APPLICATION));
		return resource;
	}


	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		obj.put(ResourceTools.ATTR_RESDESC_ID, this.id);
		obj.put(ResourceTools.ATTR_TYPE, this.type);
		obj.put(ResourceTools.ATTR_NAME, this.name);
		obj.put(ResourceTools.ATTR_VERSION, this.maxVersion.getName());
		return obj;
	}
	
}
