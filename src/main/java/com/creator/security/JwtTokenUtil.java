package com.creator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  static final String CLAIM_KEY_ID =        "sub";
  static final String CLAIM_KEY_CREATED =   "created";
  static final String CLAIM_KEY_USERNAME =  "username";
  static final String CLAIM_KEY_PASSWORD =  "password";
  static final String CLAIM_KEY_ROLE =      "role";
  static final String CLAIM_KEY_FIRSTNAME =   "firstName";
  static final String CLAIM_KEY_LASTNAME =   "lastName";
  static final String CLAIM_KEY_EMAIL =   "email";
  static final String CLAIM_KEY_VALIDITY_START =   "validityStartDate";
  static final String CLAIM_KEY_VALIDITY_END =   "validityEndDate";
  static final String CLAIM_KEY_PWD_RESET =   "lastPwdResetDate";


  static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
  private static final String SEPARATOR = ",";

  private static final String SECRET = "creator";
  private static final String PROP_KEY_SECRET = "creator_secret_key";
  private static final int EXPIRATION = 15;


  public String getUsernameFromToken(String token) {
    String username;
    try {
      final Claims claims = getClaimsFromToken(token);
      username =  (String)claims.get(CLAIM_KEY_USERNAME);
    } catch (Exception e) {
      username = null;
    }
    return username;
  }
  
  
  
  
  public String getPasswordFromToken(String token) {
    String username;
    try {
      final Claims claims = getClaimsFromToken(token);
      username =  (String)claims.get(CLAIM_KEY_PASSWORD);
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  
  private Date getDateFromToken(String token, String claimDateKey) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(claimDateKey));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }


  public Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }


  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      String secret = getSecret();
      claims = Jwts.parser()
          .setSigningKey(secret)
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }


  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + getExpiration() * 60 * 1000);
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }


  public String generateToken(UserDetails userDetails) {
    JwtUser user = (JwtUser) userDetails;
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put(CLAIM_KEY_ID, user.getId());
    claims.put(CLAIM_KEY_USERNAME, user.getUsername());
    claims.put(CLAIM_KEY_PASSWORD, user.getPassword());
    claims.put(CLAIM_KEY_ROLE, getCommaSeparatedRoles(user.getAuthorities()));
    claims.put(CLAIM_KEY_CREATED, user.getCreated());
    claims.put(CLAIM_KEY_FIRSTNAME, user.getFirstName());
    claims.put(CLAIM_KEY_LASTNAME, user.getLastName());
    claims.put(CLAIM_KEY_EMAIL, user.getEmail());
    claims.put(CLAIM_KEY_VALIDITY_START, user.getValidityStartDate());
    claims.put(CLAIM_KEY_VALIDITY_END, user.getValidityEndDate());
    claims.put(CLAIM_KEY_PWD_RESET, user.getLastPwdResetDate());
    return generateToken(claims);
  }

  private String getCommaSeparatedRoles(Collection<? extends GrantedAuthority> authorities) {
    StringBuilder stringBuilder = new StringBuilder();
    int pos = 1;
    for(GrantedAuthority authority : authorities){
      stringBuilder.append(authority.getAuthority());
      if (pos!= authorities.size()){
        stringBuilder.append(SEPARATOR);
      }
      pos ++;
    }
    return stringBuilder.toString();
  }

  String generateToken(Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, getSecret())
        .compact();
  }

  public Boolean canTokenBeRefreshed(String token) {
    final Date created = getCreatedDateFromToken(token);
    final Date lastPasswordReset = getLastPwdResetDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
        && (!isTokenExpired(token));
  }


  public String refreshToken(String token) {
    String refreshedToken;
    try {
      final Claims claims = getClaimsFromToken(token);
      claims.put(CLAIM_KEY_CREATED, new Date());
      refreshedToken = generateToken(claims);
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  public Boolean validateToken(String token) {
    final Date created = getCreatedDateFromToken(token);
    final Date lastPwdReset = getLastPwdResetDateFromToken(token);
    return (!isTokenExpired(token)
        && !isCreatedBeforeLastPasswordReset(created, lastPwdReset));
  }



  public  Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }


  private Date getLastPwdResetDateFromToken(String token) {
    Date lastPwdResetDate;
    try {
      final Claims claims = getClaimsFromToken(token);
      lastPwdResetDate = new Date((Long) claims.get(CLAIM_KEY_PWD_RESET));
    } catch (Exception e) {
      lastPwdResetDate = null;
    }
    return lastPwdResetDate;
  }



  public String getSessionIdFromToken(String token) {
    String sessionId;
    try {
      final Claims claims = getClaimsFromToken(token);
      sessionId =  (String)claims.getSubject();
    } catch (Exception e) {
      sessionId = null;
    }
    return sessionId;
  }


  private String getSecret() {
    return SECRET;
  }

  private int getExpiration() {
    try {
      String exp = "10";
      return Integer.parseInt(exp);
     
    }catch (JwtException e) {
      return EXPIRATION;
    }
  }



  /**
   * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
   * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
   * 
   * @param token the JWT token to parse
   * @return the User object extracted from specified token or null if a token is invalid.
   */
  public UserDetails parseToken(String token) {  
    try {
      if (validateToken(token)){

        Claims body = Jwts.parser()
            .setSigningKey(getSecret())
            .parseClaimsJws(token)
            .getBody();

        String id = body.getSubject();
        String username = body.get(CLAIM_KEY_USERNAME) != null ? (String) body.get(CLAIM_KEY_USERNAME): "";
        Date created = getDateFromToken(token, CLAIM_KEY_CREATED);
        String password = body.get(CLAIM_KEY_PASSWORD) != null ? (String) body.get(CLAIM_KEY_PASSWORD): "";
        String role = body.get(CLAIM_KEY_ROLE) != null ? (String) body.get(CLAIM_KEY_ROLE): "";
        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);

        String email = body.get(CLAIM_KEY_EMAIL) != null ? (String) body.get(CLAIM_KEY_EMAIL): "";
        String firstName = body.get(CLAIM_KEY_FIRSTNAME) != null ? (String) body.get(CLAIM_KEY_FIRSTNAME): "";
        String lastName = body.get(CLAIM_KEY_LASTNAME) != null ? (String) body.get(CLAIM_KEY_LASTNAME): "";
        Date validityStartDate = getDateFromToken(token, CLAIM_KEY_VALIDITY_START);
        Date validityEndDate = getDateFromToken(token, CLAIM_KEY_VALIDITY_END);
        Date lastPwdResetDate = getDateFromToken(token, CLAIM_KEY_PWD_RESET);

        UserDetails userDetails = new JwtUser.JwtUserBuilder(username, password, id)
            .authorities(authorities)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .created(created)
            .validityStartDate(validityStartDate)
            .validityEndDate(validityEndDate)
            .lastPwdResetDate(lastPwdResetDate)
            .build();
        return userDetails;
      }
      return null;
    } catch (JwtException e) {
      return null;
    } 
  }

}
