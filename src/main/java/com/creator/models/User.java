package com.creator.models;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

	private static final String ROLE_ADMIN = "ADMIN";
	
	public User() {
	}
	public User(Long id, String username,String password) {
		this.id = id;
		this.name = username;
		this.password = password;
	}
	@Id
	private long id;
	private String name;
	private String password;
	public  long getId() {
		return id;
	}
	public  String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Date getCreationDate() {
		return null;
	}
	public String getRole() {
		return ROLE_ADMIN;
	}
	  @Override
		public String toString() {
			JSONObject obj = new JSONObject();
			obj.put("id", id)
			.put("name", name)
			.put("password", password);
			return obj.toString(1);
		}
	public String getEmail() {
		return null;
	}
}
