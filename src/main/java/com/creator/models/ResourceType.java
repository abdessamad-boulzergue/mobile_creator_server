package com.creator.models;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name="resource_type")
public class ResourceType {


	public enum TYPES{
		WORKFLOW("workflow"),
		PLUGIN("plugin"),
		APPLICATION("application"),
		DOCUMENT("document");

		private final String name;

		TYPES(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public ResourceType() {
	}


	public ResourceType( TYPES types){
		this.name = types.getName();
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true)
	private String name;

	
	public String getName() {
		return name;
	}
	
	public void setName(TYPES types) {
      this.name = types.getName();
	}



	public Long getId() {
		return id;
	}


	public boolean hasName() {
		
		return name!=null && !name.trim().isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ResourceType
				&& name!=null
				&& name.equals(((ResourceType)obj).getName())
				&& id.equals(((ResourceType)obj).getId());
	}
	@Override
	public String toString() {
		return "{"
				+ "name: "+ this.name+
	         "}";
	}

}
