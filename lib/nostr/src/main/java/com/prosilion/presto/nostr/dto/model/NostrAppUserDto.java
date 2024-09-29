package com.prosilion.presto.nostr.dto.model;

import com.prosilion.presto.web.model.AppUserDtoIF;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NostrAppUserDto implements NostrAppUserDtoIF {
  AppUserDtoIF appUserDto;
  private String pubKey;

  public NostrAppUserDto(AppUserDtoIF appUserDto) {
    this.appUserDto = appUserDto;
  }

  @Override
  public Long getId() {
    return appUserDto.getId();
  }

  @Override
  public void setId(Long id) {
    appUserDto.setId(id);
  }

  @Override
  public String getUsername() {
    return appUserDto.getUsername();
  }

  @Override
  public void setUsername(String username) {
    appUserDto.setUsername(username);
  }

  @Override
  public String getPassword() {
    return appUserDto.getPassword();
  }

  @Override
  public void setPassword(String password) {
    appUserDto.setPassword(password);
  }
}
