package com.creator.security;


import com.creator.models.User;
import com.creator.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class JwtUser implements  UserDetails {

  /**
   * 
   */
  private static final long serialVersionUID = -2064537754337002673L;

  private final  String id;

  private final  String username;
  private final  String firstName;
  private  final String lastName ;
  private final  String password;
  private  final String email ;
  private  final Collection<? extends GrantedAuthority> authorities;
  private final Date created;
  //private final  UserRights rights;
  private final  Date validityStartDate;
  private final  Date validityEndDate;
  private final  Date lastPwdResetDate;
  //private final  String group;

    public JwtUser(String sessionId,List<GrantedAuthority> authorities, User usr) {

        this.id = sessionId;
        this.authorities = authorities;
        this.username = usr.getName();
        this.password =  usr.getPassword();
        this.lastName = usr.getName();
        this.firstName =  usr.getName();
        this.email =  usr.getEmail();
        this.created = usr.getCreationDate();
        this.validityStartDate = new Date();
        this.validityEndDate = new Date(2020, 1, 1);
        Date lastPwdResetDateTmp = null;
        this.lastPwdResetDate = (lastPwdResetDateTmp != null) ? lastPwdResetDateTmp : created;

    }
  public JwtUser(String sessionId, List<GrantedAuthority> authorities, JSONObject userJSONData) {
    this.id = sessionId;
    this.authorities = authorities;
    //this.group = group;
    this.username = JsonUtil.getFieldValue(userJSONData, "id");
    this.password =  JsonUtil.getFieldValue(userJSONData, "pwd");
    this.lastName = JsonUtil.getFieldValue(userJSONData, "nom");
    this.firstName = JsonUtil.getFieldValue(userJSONData, "firstName");
    this.email = JsonUtil.getFieldValue(userJSONData, "email");
    this.created = JsonUtil.getDateFromString(JsonUtil.getFieldValue(userJSONData, "creationDate"));
    //this.rights = getUserRights(userJSONData);
    this.validityStartDate = JsonUtil.getDateFromString(JsonUtil.getFieldValue(userJSONData, "debut_validite_utilisateur"));
    this.validityEndDate = JsonUtil.getDateFromString(JsonUtil.getFieldValue(userJSONData, "fin_validite_utilisateur"));
    Date lastPwdResetDateTmp = JsonUtil.getDateFromString(JsonUtil.getFieldValue(userJSONData, "date_chgt_mot_pas"));
    this.lastPwdResetDate = (lastPwdResetDateTmp != null) ? lastPwdResetDateTmp : created;
  }


  private JwtUser(JwtUserBuilder jwtUserBuilder) {
    this.id = jwtUserBuilder.id;
    this.authorities = jwtUserBuilder.authorities;
    this.username = jwtUserBuilder.username;
    this.password =  jwtUserBuilder.password;
    this.lastName = jwtUserBuilder.lastName;
    this.firstName = jwtUserBuilder.firstName;
    this.email = jwtUserBuilder.email;
    this.created = jwtUserBuilder.created;
    this.validityStartDate = jwtUserBuilder.validityStartDate;
    this.validityEndDate = jwtUserBuilder.validityEndDate;
    this.lastPwdResetDate = jwtUserBuilder.lastPwdResetDate;

  }

public static class JwtUserBuilder {

    private  String id;
    private  String username;
    private  String firstName;
    private  String lastName ;
    private  String password;
    private  String email ;
    private  Collection<? extends GrantedAuthority> authorities;
    private  Date created;
    private Date validityStartDate;
    private Date validityEndDate;
    private Date lastPwdResetDate;

    public JwtUserBuilder (String username, String password, String id){
      this.username = username;
      this.password =  password;
      this.id = id;
    }

    public JwtUserBuilder firstName(String firstName)
    {
      this.firstName = firstName;
      return this;
    }

    public JwtUserBuilder lastName(String lastName)
    {
      this.lastName = lastName;
      return this;
    }


    public JwtUserBuilder email(String email)
    {
      this.email = email;
      return this;
    }

    public JwtUserBuilder authorities(Collection<? extends GrantedAuthority> authorities )
    {
      this.authorities = authorities;
      return this;
    }

    public JwtUserBuilder created(Date created )
    {
      this.created = created;
      return this;
    }


    public JwtUserBuilder validityStartDate(Date validityStartDate)
    {
      this.validityStartDate = validityStartDate;
      return this;
    }

    public JwtUserBuilder validityEndDate(Date validityEndDate)
    {
      this.validityEndDate = validityEndDate;
      return this;
    }

    public JwtUserBuilder lastPwdResetDate(Date lastPwdResetDate)
    {
      this.lastPwdResetDate = lastPwdResetDate;
      return this;
    }



    public JwtUser build ()
    {
      return new JwtUser(this);
    }

  }
  
  

  public static JwtUser getPrincipal() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    JwtUser user = (JwtUser)auth.getPrincipal();
    return user;
  }



  @JsonIgnore
  public  String getId() {
    return id;
  }

  public  String getUsername() {
    return username;
  }

  @JsonIgnore
  public  boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  public  boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  public  boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  public  String getPassword() {
    return password;
  }


  @Override
  public boolean isEnabled() {
    return true;
  }


  public  Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the created
   */
  public Date getCreated() {
    return created;
  }

  /**
   * @return the validityStartDate
   */
  public Date getValidityStartDate() {
    return validityStartDate;
  }

  /**
   * @return the validityEndDate
   */
  public Date getValidityEndDate() {
    return validityEndDate;
  }

  /**
   * @return the lastPwdResetDate
   */
  public Date getLastPwdResetDate() {
    return lastPwdResetDate;
  }

}
