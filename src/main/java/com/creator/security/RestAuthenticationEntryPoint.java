package com.creator.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public final class RestAuthenticationEntryPoint 
implements AuthenticationEntryPoint, Serializable {

	  private static final long serialVersionUID = -8970718410437077606L;


	  public void commence(HttpServletRequest request,
	      HttpServletResponse response,
	      AuthenticationException authException) throws IOException {
	    // This is invoked when user tries to access a secured REST resource without supplying any credentials
	    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to

	    //avoid CORS issues
	    response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
	    response.addHeader("Access-Control-Allow-Credentials", "true");
	    response.addHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");

	    if (org.springframework.http.HttpMethod.OPTIONS.equals(request.getMethod())) {
			response.setStatus(200);
	    }

	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	  }
	}