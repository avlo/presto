package com.prosilion.presto.nostr.db;

import com.prosilion.presto.nostr.entity.NostrUser;
import com.prosilion.presto.security.entity.NostrUserDetails;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NostrJdbcDaoImpl extends JdbcDaoSupport implements UserDetailsService, MessageSourceAware {
  public static final String NOSTR_USER_SCHEMA_DDL_LOCATION = "com/prosilion/presto/nostr/db/users.sql";
  public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,pubkey,enabled from users where username = ?";
  public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select username,authority from authorities where username = ?";
  public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority from groups g, group_members gm, group_authorities ga where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
  private String authoritiesByUsernameQuery = "select username,authority from authorities where username = ?";
  private String groupAuthoritiesByUsernameQuery = "select g.id, g.group_name, ga.authority from groups g, group_members gm, group_authorities ga where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
  private String usersByUsernameQuery = "select username,password,pubkey,enabled from users where username = ?";
  private String rolePrefix = "";
  private boolean usernameBasedPrimaryKey = true;
  private boolean enableAuthorities = true;
  private boolean enableGroups;

  public NostrJdbcDaoImpl() {
  }

  protected MessageSourceAccessor getMessages() {
    return this.messages;
  }

  protected void addCustomAuthorities(String username, List<GrantedAuthority> authorities) {
  }

  public String getUsersByUsernameQuery() {
    return this.usersByUsernameQuery;
  }

  protected void initDao() throws ApplicationContextException {
    Assert.isTrue(this.enableAuthorities || this.enableGroups, "Use of either authorities or groups must be enabled");
  }

  @Override
  public NostrUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<NostrUserDetails> users = this.loadUsersByUsername(username);
    if (users.size() == 0) {
      this.logger.debug("Query returned no results for user '" + username + "'");
      throw new UsernameNotFoundException(this.messages.getMessage("NostrJdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));
    } else {
      NostrUserDetails user = (NostrUserDetails) users.get(0);
      Set<GrantedAuthority> dbAuthsSet = new HashSet();
      if (this.enableAuthorities) {
        dbAuthsSet.addAll(this.loadUserAuthorities(user.getUsername()));
      }

      if (this.enableGroups) {
        dbAuthsSet.addAll(this.loadGroupAuthorities(user.getUsername()));
      }

      List<GrantedAuthority> dbAuths = new ArrayList(dbAuthsSet);
      this.addCustomAuthorities(user.getUsername(), dbAuths);
      if (dbAuths.size() == 0) {
        this.logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");
        throw new UsernameNotFoundException(this.messages.getMessage("JdbcDaoImpl.noAuthority", new Object[]{username}, "User {0} has no GrantedAuthority"));
      } else {
        return this.createUserDetails(username, user, dbAuths);
      }
    }
  }

  protected List<NostrUserDetails> loadUsersByUsername(String username) {
    RowMapper<NostrUserDetails> mapper = (rs, rowNum) -> {
      String username1 = rs.getString(1);
      String password = rs.getString(2);
      String pubkey = rs.getString(3);
      boolean enabled = rs.getBoolean(4);
      return new NostrUser(username1, password, pubkey, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
    };
    return this.getJdbcTemplate().query(this.usersByUsernameQuery, mapper, new Object[]{username});
  }

  protected List<GrantedAuthority> loadUserAuthorities(String username) {
    return this.getJdbcTemplate().query(this.authoritiesByUsernameQuery, new String[]{username}, (rs, rowNum) -> {
      String var10000 = this.rolePrefix;
      String roleName = var10000 + rs.getString(2);
      return new SimpleGrantedAuthority(roleName);
    });
  }

  protected List<GrantedAuthority> loadGroupAuthorities(String username) {
    return this.getJdbcTemplate().query(this.groupAuthoritiesByUsernameQuery, new String[]{username}, (rs, rowNum) -> {
      String var10000 = this.getRolePrefix();
      String roleName = var10000 + rs.getString(3);
      return new SimpleGrantedAuthority(roleName);
    });
  }

  protected NostrUserDetails createUserDetails(String username, NostrUserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
    String returnUsername = userFromUserQuery.getUsername();
    if (!this.usernameBasedPrimaryKey) {
      returnUsername = username;
    }

    return new NostrUser(returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.getPubkey(), userFromUserQuery.isEnabled(), userFromUserQuery.isAccountNonExpired(), userFromUserQuery.isCredentialsNonExpired(), userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
  }

  public void setAuthoritiesByUsernameQuery(String queryString) {
    this.authoritiesByUsernameQuery = queryString;
  }

  protected String getAuthoritiesByUsernameQuery() {
    return this.authoritiesByUsernameQuery;
  }

  public void setGroupAuthoritiesByUsernameQuery(String queryString) {
    this.groupAuthoritiesByUsernameQuery = queryString;
  }

  public void setRolePrefix(String rolePrefix) {
    this.rolePrefix = rolePrefix;
  }

  protected String getRolePrefix() {
    return this.rolePrefix;
  }

  public void setUsernameBasedPrimaryKey(boolean usernameBasedPrimaryKey) {
    this.usernameBasedPrimaryKey = usernameBasedPrimaryKey;
  }

  protected boolean isUsernameBasedPrimaryKey() {
    return this.usernameBasedPrimaryKey;
  }

  public void setUsersByUsernameQuery(String usersByUsernameQueryString) {
    this.usersByUsernameQuery = usersByUsernameQueryString;
  }

  protected boolean getEnableAuthorities() {
    return this.enableAuthorities;
  }

  public void setEnableAuthorities(boolean enableAuthorities) {
    this.enableAuthorities = enableAuthorities;
  }

  protected boolean getEnableGroups() {
    return this.enableGroups;
  }

  public void setEnableGroups(boolean enableGroups) {
    this.enableGroups = enableGroups;
  }

  public void setMessageSource(MessageSource messageSource) {
    Assert.notNull(messageSource, "messageSource cannot be null");
    this.messages = new MessageSourceAccessor(messageSource);
  }
}
