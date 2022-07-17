package com.creator.rest.dto;

public class JwtAuthenticationResponse {

	  /**
	   * 
	   */
	  private static final long serialVersionUID = 3200477612875447696L;

	  private String token;
	  public JwtAuthenticationResponse(String token) {
	    this.token = token;
	  }

	  /**
	   * @return the token
	   */
	  public String getToken() {
	    return token;
	  }

	  /**
	   * @param token the token to set
	   */
	  public void setToken(String token) {
	    this.token = token;
	  }

}
