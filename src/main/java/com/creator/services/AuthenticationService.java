package com.creator.services;


import com.creator.models.User;
import com.creator.security.JwtTokenUtil;
import com.creator.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class AuthenticationService implements IAuthentication{


	@Autowired
	  private JwtTokenUtil jwtTokenUtil;
	
	  private static final String ROLE_PREFIX   = "ROLE_";

	@Override
	public String login(String username, String password) {
		
		
		String token =null;
		try {
			
			User usr = new User(1L, username, password);
			System.out.println("usr : "+ usr +", username:"+username +", pass:"+ password+"#");
			if(usr!=null && usr.getPassword().equals(password)){
			
			String role = (ROLE_PREFIX + usr.getRole()).toUpperCase();
		      List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
			 UserDetails userDetails = new JwtUser("1", authorities, usr);

		      // Issue a token
		       token = jwtTokenUtil.generateToken(userDetails);

		      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		      SecurityContextHolder.getContext().setAuthentication(authentication);

			}
			
		} catch (NoSuchElementException  e) {
		}
		
		return token;
	}

}
