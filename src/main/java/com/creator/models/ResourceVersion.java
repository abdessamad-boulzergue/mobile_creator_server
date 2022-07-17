package com.creator.models;

import javax.persistence.*;

@Entity
@Table(name="resource_version")
public class ResourceVersion {

	public static final String DEFAULT_VERSION_NAME = "1.0";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;

	@ManyToOne
	@JoinColumn(name="resource_id")
	private Resource resource;
	
	public ResourceVersion() {
		this.name = DEFAULT_VERSION_NAME;
	}
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	
	
	public static  String getNextVersionName(String version) {
		String versionName = version.replace(".", "");
		int versionValue= Integer.parseInt(versionName) + 1;
		versionName = String.valueOf(versionValue);
		char[] chars = versionName.toCharArray();
		versionName = new String();
		for(int i=0; i<chars.length ; i++) {
			versionName = versionName.concat(String.valueOf(chars[i]));
			if(i<chars.length-1)
				versionName = versionName.concat(".");
		}
		 return versionName;
	}
	
	public String getNextVersionName() {
		 return getNextVersionName(this.name);
	}

	@Override
	public boolean equals(Object version){
		return (version instanceof ResourceVersion) && ((ResourceVersion) version).getId().equals(this.id);
	}
}
