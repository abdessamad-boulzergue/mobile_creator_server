package com.creator.rest.dto;

import org.json.JSONObject;

public class Credentials {
	  
	  /**
	   * 
	   */
	  private static final long serialVersionUID = -6415737974521493014L;

	  private String username;
	  
	  private String password;
	  
	  
	  /**
	   * @return the username
	   */
	  public String getUsername() {
	    return username;
	  }
	  /**
	   * @param username the username to set
	   */
	  public void setUsername(String username) {
	    this.username = username;
	  }
	  /**
	   * @return the password
	   */
	  public String getPassword() {
	    return password;
	  }
	  /**
	   * @param password the password to set
	   */
	  public void setPassword(String password) {
	    this.password = password;
	  }

	  @Override
	public String toString() {
		JSONObject obj = new JSONObject();
		obj.put("username", username).put("password", password);
		return obj.toString(1);
	}
}
