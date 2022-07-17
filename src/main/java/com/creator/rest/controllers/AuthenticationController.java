package com.creator.rest.controllers;


import com.creator.rest.dto.Credentials;
import com.creator.rest.dto.JwtAuthenticationResponse;
import com.creator.services.IAuthentication;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "creator/api/v1/auth")
@CrossOrigin
public class AuthenticationController {
	
	@Autowired
	IAuthentication authService;
	
	
	@PostMapping(path ="")
	public ResponseEntity  login(@RequestBody Credentials credential){
		String username = credential.getUsername();
		String password = credential.getPassword();
		String token =  authService.login(username, password);
		
		 if (token!=null){
		       return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new JwtAuthenticationResponse(token));
		}

		 JSONObject error = new JSONObject();
	     error.put("message", "Authentication failed, please contact your admin");
	     error.put("path", "/login");
		 return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.toString(1)) ;

	}
}
