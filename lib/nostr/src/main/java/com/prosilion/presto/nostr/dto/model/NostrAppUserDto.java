package com.prosilion.presto.nostr.dto.model;

import com.prosilion.presto.web.model.NostrAppUserDtoIF;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NostrAppUserDto implements NostrAppUserDtoIF {
  private Long id;
  private String username;
  private String password;
  private String pubkey;
}
