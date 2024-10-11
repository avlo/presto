package com.prosilion.presto.nostr.db;

import com.prosilion.presto.nostr.entity.NostrUser;
import com.prosilion.presto.security.entity.NostrUserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.log.LogMessage;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class NostrJdbcUserDetailsManager extends NostrJdbcDaoImpl implements NostrUserDetailsManager, GroupManager {
  public static final String DEF_CREATE_USER_SQL = "insert into users (username, password, pubkey, enabled) values (?,?,?,?)";
  public static final String DEF_DELETE_USER_SQL = "delete from users where username = ?";
  public static final String DEF_UPDATE_USER_SQL = "update users set password = ?, pubkey = ?, enabled = ? where username = ?";
  public static final String DEF_INSERT_AUTHORITY_SQL = "insert into authorities (username, authority) values (?,?)";
  public static final String DEF_DELETE_USER_AUTHORITIES_SQL = "delete from authorities where username = ?";
  public static final String DEF_USER_EXISTS_SQL = "select username from users where username = ?";
  public static final String DEF_CHANGE_PASSWORD_SQL = "update users set password = ? where username = ?";
  public static final String DEF_FIND_GROUPS_SQL = "select group_name from groups";
  public static final String DEF_FIND_USERS_IN_GROUP_SQL = "select username from group_members gm, groups g where gm.group_id = g.id and g.group_name = ?";
  public static final String DEF_INSERT_GROUP_SQL = "insert into groups (group_name) values (?)";
  public static final String DEF_FIND_GROUP_ID_SQL = "select id from groups where group_name = ?";
  public static final String DEF_INSERT_GROUP_AUTHORITY_SQL = "insert into group_authorities (group_id, authority) values (?,?)";
  public static final String DEF_DELETE_GROUP_SQL = "delete from groups where id = ?";
  public static final String DEF_DELETE_GROUP_AUTHORITIES_SQL = "delete from group_authorities where group_id = ?";
  public static final String DEF_DELETE_GROUP_MEMBERS_SQL = "delete from group_members where group_id = ?";
  public static final String DEF_RENAME_GROUP_SQL = "update groups set group_name = ? where group_name = ?";
  public static final String DEF_INSERT_GROUP_MEMBER_SQL = "insert into group_members (group_id, username) values (?,?)";
  public static final String DEF_DELETE_GROUP_MEMBER_SQL = "delete from group_members where group_id = ? and username = ?";
  public static final String DEF_GROUP_AUTHORITIES_QUERY_SQL = "select g.id, g.group_name, ga.authority from groups g, group_authorities ga where g.group_name = ? and g.id = ga.group_id ";
  public static final String DEF_DELETE_GROUP_AUTHORITY_SQL = "delete from group_authorities where group_id = ? and authority = ?";
  protected final Log logger = LogFactory.getLog(this.getClass());
  private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
  private String createUserSql = "insert into users (username, password, pubkey, enabled) values (?,?,?,?)";
  private String deleteUserSql = "delete from users where username = ?";
  private String updateUserSql = "update users set password = ?, pubkey = ?, enabled = ? where username = ?";
  private String createAuthoritySql = "insert into authorities (username, authority) values (?,?)";
  private String deleteUserAuthoritiesSql = "delete from authorities where username = ?";
  private String userExistsSql = "select username from users where username = ?";
  private String changePasswordSql = "update users set password = ? where username = ?";
  private String findAllGroupsSql = "select group_name from groups";
  private String findUsersInGroupSql = "select username from group_members gm, groups g where gm.group_id = g.id and g.group_name = ?";
  private String insertGroupSql = "insert into groups (group_name) values (?)";
  private String findGroupIdSql = "select id from groups where group_name = ?";
  private String insertGroupAuthoritySql = "insert into group_authorities (group_id, authority) values (?,?)";
  private String deleteGroupSql = "delete from groups where id = ?";
  private String deleteGroupAuthoritiesSql = "delete from group_authorities where group_id = ?";
  private String deleteGroupMembersSql = "delete from group_members where group_id = ?";
  private String renameGroupSql = "update groups set group_name = ? where group_name = ?";
  private String insertGroupMemberSql = "insert into group_members (group_id, username) values (?,?)";
  private String deleteGroupMemberSql = "delete from group_members where group_id = ? and username = ?";
  private String groupAuthoritiesSql = "select g.id, g.group_name, ga.authority from groups g, group_authorities ga where g.group_name = ? and g.id = ga.group_id ";
  private String deleteGroupAuthoritySql = "delete from group_authorities where group_id = ? and authority = ?";
  private AuthenticationManager authenticationManager;
  private UserCache userCache = new NullUserCache();

  public NostrJdbcUserDetailsManager() {
  }

  public NostrJdbcUserDetailsManager(DataSource dataSource) {
    this.setDataSource(dataSource);
  }

  protected void initDao() throws ApplicationContextException {
    if (this.authenticationManager == null) {
      this.logger.info("No authentication manager set. Reauthentication of users when changing passwords will not be performed.");
    }

    super.initDao();
  }

  protected List<NostrUserDetails> loadUsersByUsername(String username) {
    List<NostrUserDetails> query = this.getJdbcTemplate().query(this.getUsersByUsernameQuery(), this::mapToUser, new Object[]{username});
    return query;
  }

  protected List<NostrUserDetails> loadUsersByPubkey(String pubkey) {
    List<NostrUserDetails> query = this.getJdbcTemplate().query(this.getUsersByPubkeyQuery(), this::mapToUser, new Object[]{pubkey});
    return query;
  }

  private NostrUserDetails mapToUser(ResultSet rs, int rowNum) throws SQLException {
    String userName = rs.getString(1);
    String password = rs.getString(2);
    String pubkey = rs.getString(3);
    boolean enabled = rs.getBoolean(4);
    boolean accLocked = false;
    boolean accExpired = false;
    boolean credsExpired = false;
    if (rs.getMetaData().getColumnCount() > 4) {
      accLocked = rs.getBoolean(5);
      accExpired = rs.getBoolean(6);
      credsExpired = rs.getBoolean(7);
    }

    NostrUser nostrUser = new NostrUser(userName, password, pubkey, enabled, !accExpired, !credsExpired, !accLocked, AuthorityUtils.NO_AUTHORITIES);
    return nostrUser;
  }

  public void createUser(final NostrUserDetails nostrUserDetails) {
    this.validateUserDetails(nostrUserDetails);
    this.getJdbcTemplate().update(this.createUserSql, (ps) -> {
      ps.setString(1, nostrUserDetails.getUsername());
      ps.setString(2, nostrUserDetails.getPassword());
      ps.setString(3, nostrUserDetails.getPubkey());
      ps.setBoolean(4, nostrUserDetails.isEnabled());
      int paramCount = ps.getParameterMetaData().getParameterCount();
      if (paramCount > 4) {
        ps.setBoolean(5, !nostrUserDetails.isAccountNonLocked());
        ps.setBoolean(6, !nostrUserDetails.isAccountNonExpired());
        ps.setBoolean(7, !nostrUserDetails.isCredentialsNonExpired());
      }

    });
    if (this.getEnableAuthorities()) {
      this.insertUserAuthorities(nostrUserDetails);
    }

  }

  public void updateUser(final NostrUserDetails nostrUserDetails) {
    this.validateUserDetails(nostrUserDetails);
    this.getJdbcTemplate().update(this.updateUserSql, (ps) -> {
      ps.setString(1, nostrUserDetails.getPassword());
      ps.setString(2, nostrUserDetails.getPubkey());
      ps.setBoolean(3, nostrUserDetails.isEnabled());
      int paramCount = ps.getParameterMetaData().getParameterCount();
      if (paramCount == 4) {
        ps.setString(4, nostrUserDetails.getUsername());
      } else {
        ps.setBoolean(4, !nostrUserDetails.isAccountNonLocked());
        ps.setBoolean(5, !nostrUserDetails.isAccountNonExpired());
        ps.setBoolean(6, !nostrUserDetails.isCredentialsNonExpired());
        ps.setString(7, nostrUserDetails.getUsername());
      }

    });
    if (this.getEnableAuthorities()) {
      this.deleteUserAuthorities(nostrUserDetails.getUsername());
      this.insertUserAuthorities(nostrUserDetails);
    }

    this.userCache.removeUserFromCache(nostrUserDetails.getUsername());
  }

  private void insertUserAuthorities(NostrUserDetails user) {
    Iterator var2 = user.getAuthorities().iterator();

    while (var2.hasNext()) {
      GrantedAuthority auth = (GrantedAuthority) var2.next();
      this.getJdbcTemplate().update(this.createAuthoritySql, new Object[]{user.getUsername(), auth.getAuthority()});
    }

  }

  public void deleteUser(String username) {
    if (this.getEnableAuthorities()) {
      this.deleteUserAuthorities(username);
    }

    this.getJdbcTemplate().update(this.deleteUserSql, new Object[]{username});
    this.userCache.removeUserFromCache(username);
  }

  private void deleteUserAuthorities(String username) {
    this.getJdbcTemplate().update(this.deleteUserAuthoritiesSql, new Object[]{username});
  }

  public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
    Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
    if (currentUser == null) {
      throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
    } else {
      String username = currentUser.getName();
      if (this.authenticationManager != null) {
        this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
        this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
      } else {
        this.logger.debug("No authentication manager set. Password won't be re-checked.");
      }

      this.logger.debug("Changing password for user '" + username + "'");
      this.getJdbcTemplate().update(this.changePasswordSql, new Object[]{newPassword, username});
      Authentication authentication = this.createNewAuthentication(currentUser, newPassword);
      SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
      context.setAuthentication(authentication);
      this.securityContextHolderStrategy.setContext(context);
      this.userCache.removeUserFromCache(username);
    }
  }

  protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
    NostrUserDetails user = this.loadUserByUsername(currentAuth.getName());
    UsernamePasswordAuthenticationToken newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user, (Object) null, user.getAuthorities());
    newAuthentication.setDetails(currentAuth.getDetails());
    return newAuthentication;
  }

  public boolean userExists(String username) {
    List<String> users = this.getJdbcTemplate().queryForList(this.userExistsSql, new String[]{username}, String.class);
    if (users.size() > 1) {
      throw new IncorrectResultSizeDataAccessException("More than one user found with name '" + username + "'", 1);
    } else {
      return users.size() == 1;
    }
  }

  public List<String> findAllGroups() {
    return this.getJdbcTemplate().queryForList(this.findAllGroupsSql, String.class);
  }

  public List<String> findUsersInGroup(String groupName) {
    Assert.hasText(groupName, "groupName should have text");
    return this.getJdbcTemplate().queryForList(this.findUsersInGroupSql, new String[]{groupName}, String.class);
  }

  public void createGroup(final String groupName, final List<GrantedAuthority> authorities) {
    Assert.hasText(groupName, "groupName should have text");
    Assert.notNull(authorities, "authorities cannot be null");
    this.logger.debug("Creating new group '" + groupName + "' with authorities " + AuthorityUtils.authorityListToSet(authorities));
    this.getJdbcTemplate().update(this.insertGroupSql, new Object[]{groupName});
    int groupId = this.findGroupId(groupName);
    Iterator var4 = authorities.iterator();

    while (var4.hasNext()) {
      GrantedAuthority a = (GrantedAuthority) var4.next();
      String authority = a.getAuthority();
      this.getJdbcTemplate().update(this.insertGroupAuthoritySql, (ps) -> {
        ps.setInt(1, groupId);
        ps.setString(2, authority);
      });
    }

  }

  public void deleteGroup(String groupName) {
    this.logger.debug("Deleting group '" + groupName + "'");
    Assert.hasText(groupName, "groupName should have text");
    int id = this.findGroupId(groupName);
    PreparedStatementSetter groupIdPSS = (ps) -> {
      ps.setInt(1, id);
    };
    this.getJdbcTemplate().update(this.deleteGroupMembersSql, groupIdPSS);
    this.getJdbcTemplate().update(this.deleteGroupAuthoritiesSql, groupIdPSS);
    this.getJdbcTemplate().update(this.deleteGroupSql, groupIdPSS);
  }

  public void renameGroup(String oldName, String newName) {
    this.logger.debug("Changing group name from '" + oldName + "' to '" + newName + "'");
    Assert.hasText(oldName, "oldName should have text");
    Assert.hasText(newName, "newName should have text");
    this.getJdbcTemplate().update(this.renameGroupSql, new Object[]{newName, oldName});
  }

  public void addUserToGroup(final String username, final String groupName) {
    this.logger.debug("Adding user '" + username + "' to group '" + groupName + "'");
    Assert.hasText(username, "username should have text");
    Assert.hasText(groupName, "groupName should have text");
    int id = this.findGroupId(groupName);
    this.getJdbcTemplate().update(this.insertGroupMemberSql, (ps) -> {
      ps.setInt(1, id);
      ps.setString(2, username);
    });
    this.userCache.removeUserFromCache(username);
  }

  public void removeUserFromGroup(final String username, final String groupName) {
    this.logger.debug("Removing user '" + username + "' to group '" + groupName + "'");
    Assert.hasText(username, "username should have text");
    Assert.hasText(groupName, "groupName should have text");
    int id = this.findGroupId(groupName);
    this.getJdbcTemplate().update(this.deleteGroupMemberSql, (ps) -> {
      ps.setInt(1, id);
      ps.setString(2, username);
    });
    this.userCache.removeUserFromCache(username);
  }

  public List<GrantedAuthority> findGroupAuthorities(String groupName) {
    this.logger.debug("Loading authorities for group '" + groupName + "'");
    Assert.hasText(groupName, "groupName should have text");
    return this.getJdbcTemplate().query(this.groupAuthoritiesSql, new String[]{groupName}, this::mapToGrantedAuthority);
  }

  private GrantedAuthority mapToGrantedAuthority(ResultSet rs, int rowNum) throws SQLException {
    String var10000 = this.getRolePrefix();
    String roleName = var10000 + rs.getString(3);
    return new SimpleGrantedAuthority(roleName);
  }

  public void removeGroupAuthority(String groupName, final GrantedAuthority authority) {
    this.logger.debug("Removing authority '" + authority + "' from group '" + groupName + "'");
    Assert.hasText(groupName, "groupName should have text");
    Assert.notNull(authority, "authority cannot be null");
    int id = this.findGroupId(groupName);
    this.getJdbcTemplate().update(this.deleteGroupAuthoritySql, (ps) -> {
      ps.setInt(1, id);
      ps.setString(2, authority.getAuthority());
    });
  }

  public void addGroupAuthority(final String groupName, final GrantedAuthority authority) {
    this.logger.debug("Adding authority '" + authority + "' to group '" + groupName + "'");
    Assert.hasText(groupName, "groupName should have text");
    Assert.notNull(authority, "authority cannot be null");
    int id = this.findGroupId(groupName);
    this.getJdbcTemplate().update(this.insertGroupAuthoritySql, (ps) -> {
      ps.setInt(1, id);
      ps.setString(2, authority.getAuthority());
    });
  }

  private int findGroupId(String group) {
    return (Integer) this.getJdbcTemplate().queryForObject(this.findGroupIdSql, Integer.class, new Object[]{group});
  }

  public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
    Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
    this.securityContextHolderStrategy = securityContextHolderStrategy;
  }

  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public void setCreateUserSql(String createUserSql) {
    Assert.hasText(createUserSql, "createUserSql should have text");
    this.createUserSql = createUserSql;
  }

  public void setDeleteUserSql(String deleteUserSql) {
    Assert.hasText(deleteUserSql, "deleteUserSql should have text");
    this.deleteUserSql = deleteUserSql;
  }

  public void setUpdateUserSql(String updateUserSql) {
    Assert.hasText(updateUserSql, "updateUserSql should have text");
    this.updateUserSql = updateUserSql;
  }

  public void setCreateAuthoritySql(String createAuthoritySql) {
    Assert.hasText(createAuthoritySql, "createAuthoritySql should have text");
    this.createAuthoritySql = createAuthoritySql;
  }

  public void setDeleteUserAuthoritiesSql(String deleteUserAuthoritiesSql) {
    Assert.hasText(deleteUserAuthoritiesSql, "deleteUserAuthoritiesSql should have text");
    this.deleteUserAuthoritiesSql = deleteUserAuthoritiesSql;
  }

  public void setUserExistsSql(String userExistsSql) {
    Assert.hasText(userExistsSql, "userExistsSql should have text");
    this.userExistsSql = userExistsSql;
  }

  public void setChangePasswordSql(String changePasswordSql) {
    Assert.hasText(changePasswordSql, "changePasswordSql should have text");
    this.changePasswordSql = changePasswordSql;
  }

  public void setFindAllGroupsSql(String findAllGroupsSql) {
    Assert.hasText(findAllGroupsSql, "findAllGroupsSql should have text");
    this.findAllGroupsSql = findAllGroupsSql;
  }

  public void setFindUsersInGroupSql(String findUsersInGroupSql) {
    Assert.hasText(findUsersInGroupSql, "findUsersInGroupSql should have text");
    this.findUsersInGroupSql = findUsersInGroupSql;
  }

  public void setInsertGroupSql(String insertGroupSql) {
    Assert.hasText(insertGroupSql, "insertGroupSql should have text");
    this.insertGroupSql = insertGroupSql;
  }

  public void setFindGroupIdSql(String findGroupIdSql) {
    Assert.hasText(findGroupIdSql, "findGroupIdSql should have text");
    this.findGroupIdSql = findGroupIdSql;
  }

  public void setInsertGroupAuthoritySql(String insertGroupAuthoritySql) {
    Assert.hasText(insertGroupAuthoritySql, "insertGroupAuthoritySql should have text");
    this.insertGroupAuthoritySql = insertGroupAuthoritySql;
  }

  public void setDeleteGroupSql(String deleteGroupSql) {
    Assert.hasText(deleteGroupSql, "deleteGroupSql should have text");
    this.deleteGroupSql = deleteGroupSql;
  }

  public void setDeleteGroupAuthoritiesSql(String deleteGroupAuthoritiesSql) {
    Assert.hasText(deleteGroupAuthoritiesSql, "deleteGroupAuthoritiesSql should have text");
    this.deleteGroupAuthoritiesSql = deleteGroupAuthoritiesSql;
  }

  public void setDeleteGroupMembersSql(String deleteGroupMembersSql) {
    Assert.hasText(deleteGroupMembersSql, "deleteGroupMembersSql should have text");
    this.deleteGroupMembersSql = deleteGroupMembersSql;
  }

  public void setRenameGroupSql(String renameGroupSql) {
    Assert.hasText(renameGroupSql, "renameGroupSql should have text");
    this.renameGroupSql = renameGroupSql;
  }

  public void setInsertGroupMemberSql(String insertGroupMemberSql) {
    Assert.hasText(insertGroupMemberSql, "insertGroupMemberSql should have text");
    this.insertGroupMemberSql = insertGroupMemberSql;
  }

  public void setDeleteGroupMemberSql(String deleteGroupMemberSql) {
    Assert.hasText(deleteGroupMemberSql, "deleteGroupMemberSql should have text");
    this.deleteGroupMemberSql = deleteGroupMemberSql;
  }

  public void setGroupAuthoritiesSql(String groupAuthoritiesSql) {
    Assert.hasText(groupAuthoritiesSql, "groupAuthoritiesSql should have text");
    this.groupAuthoritiesSql = groupAuthoritiesSql;
  }

  public void setDeleteGroupAuthoritySql(String deleteGroupAuthoritySql) {
    Assert.hasText(deleteGroupAuthoritySql, "deleteGroupAuthoritySql should have text");
    this.deleteGroupAuthoritySql = deleteGroupAuthoritySql;
  }

  public void setUserCache(UserCache userCache) {
    Assert.notNull(userCache, "userCache cannot be null");
    this.userCache = userCache;
  }

  private void validateUserDetails(NostrUserDetails user) {
    Assert.hasText(user.getUsername(), "Username may not be empty or null");
    this.validateAuthorities(user.getAuthorities());
  }

  private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
    Assert.notNull(authorities, "Authorities list must not be null");
    Iterator var2 = authorities.iterator();

    while (var2.hasNext()) {
      GrantedAuthority authority = (GrantedAuthority) var2.next();
      Assert.notNull(authority, "Authorities list contains a null entry");
      Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
    }

  }
}
