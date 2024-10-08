package com.prosilion.presto.nostr.entity;

import com.prosilion.presto.security.entity.NostrUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class NostrUser extends User implements NostrUserDetails, CredentialsContainer {
  public String pubkey;

  public NostrUser(String username, String password, String pubkey, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.pubkey = pubkey;
  }

  public NostrUser(String username, String password, String pubkey, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.pubkey = pubkey;
  }

  public static NostrUserBuilder withNostrUsername(String username) {
    return nostrUserBuilder().username(username);
  }

  public static NostrUserBuilder nostrUserBuilder() {
    return new NostrUserBuilder();
  }

  public static final class NostrUserBuilder {
    private String username;
    private String password;
    private String pubkey;
    private List<GrantedAuthority> authorities = new ArrayList();
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private boolean disabled;
    private Function<String, String> passwordEncoder = (password) -> {
      return password;
    };

    private NostrUserBuilder() {
    }

    public NostrUser.NostrUserBuilder username(String username) {
      Assert.notNull(username, "username cannot be null");
      this.username = username;
      return this;
    }

    public NostrUser.NostrUserBuilder password(String password) {
      Assert.notNull(password, "password cannot be null");
      this.password = password;
      return this;
    }

    public NostrUser.NostrUserBuilder pubkey(String pubkey) {
      Assert.notNull(pubkey, "pubkey cannot be null");
      this.pubkey = pubkey;
      return this;
    }

    public NostrUser.NostrUserBuilder passwordEncoder(Function<String, String> encoder) {
      Assert.notNull(encoder, "encoder cannot be null");
      this.passwordEncoder = encoder;
      return this;
    }

    public NostrUser.NostrUserBuilder roles(String... roles) {
      List<GrantedAuthority> authorities = new ArrayList(roles.length);
      String[] var3 = roles;
      int var4 = roles.length;

      for (int var5 = 0; var5 < var4; ++var5) {
        String role = var3[var5];
        Assert.isTrue(!role.startsWith("ROLE_"), () -> {
          return role + " cannot start with ROLE_ (it is automatically added)";
        });
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
      }

      return this.authorities((Collection) authorities);
    }

    public NostrUser.NostrUserBuilder authorities(GrantedAuthority... authorities) {
      Assert.notNull(authorities, "authorities cannot be null");
      return this.authorities((Collection) Arrays.asList(authorities));
    }

    public NostrUser.NostrUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
      Assert.notNull(authorities, "authorities cannot be null");
      this.authorities = new ArrayList(authorities);
      return this;
    }

    public NostrUser.NostrUserBuilder authorities(String... authorities) {
      Assert.notNull(authorities, "authorities cannot be null");
      return this.authorities((Collection) AuthorityUtils.createAuthorityList(authorities));
    }

    public NostrUser.NostrUserBuilder accountExpired(boolean accountExpired) {
      this.accountExpired = accountExpired;
      return this;
    }

    public NostrUser.NostrUserBuilder accountLocked(boolean accountLocked) {
      this.accountLocked = accountLocked;
      return this;
    }

    public NostrUser.NostrUserBuilder credentialsExpired(boolean credentialsExpired) {
      this.credentialsExpired = credentialsExpired;
      return this;
    }

    public NostrUser.NostrUserBuilder disabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    public NostrUserDetails build() {
      String encodedPassword = (String) this.passwordEncoder.apply(this.password);
      NostrUser nostrUser = new NostrUser(this.username, encodedPassword, this.pubkey, !this.disabled, !this.accountExpired, !this.credentialsExpired, !this.accountLocked, this.authorities);
      return nostrUser;
    }
  }
}
