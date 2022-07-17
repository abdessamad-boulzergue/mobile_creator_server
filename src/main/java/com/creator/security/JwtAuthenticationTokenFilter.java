package com.creator.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    private static final String AUTHENTICATION_SCHEME = "Bearer ";
    private static final String AUTHENTICATION_SCHEME_SOCKET_SJ = "Bearer_";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String tokenHeader = request.getHeader("AUTHORIZATION");
        final String tokenHeaderForSocket = request.getHeader("sec-websocket-protocol");

        String username = null;
        String authToken = null;
        if (tokenHeader != null && tokenHeader.startsWith(AUTHENTICATION_SCHEME)) {
        	authToken = tokenHeader.substring(7);
            username = this.getUserNameFromToken(tokenHeader);
        }else if(tokenHeaderForSocket != null && tokenHeaderForSocket.startsWith(AUTHENTICATION_SCHEME_SOCKET_SJ)){
        	authToken = tokenHeaderForSocket.substring(7);
        	username = this.getUserNameFromToken(tokenHeaderForSocket);
        }
        else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

        logger.info("checking authentication for user " + username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            UserDetails userDetails = jwtTokenUtil.parseToken(authToken);

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (userDetails!=null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
}
    String getUserNameFromToken(String tokenHeader) {
    	
    	String username= null;
    	String authToken = tokenHeader.substring(7);
        try {
            username = jwtTokenUtil.getUsernameFromToken(authToken);
        } catch (IllegalArgumentException e) {
            logger.error("an error occured during getting username from token", e);
        } catch (Exception e) {
            logger.warn("the token is expired and not valid anymore", e);
        }   
        return username;
    }

}
