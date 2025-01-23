package com.prosilion.presto.model.dto;

import com.prosilion.presto.model.entity.ExampleLdapUser;
import com.prosilion.presto.web.model.AppUserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ExampleLdapUserDto extends AppUserDto {
  private String customUserField;
  public ExampleLdapUser convertToExampleUser() {
    ExampleLdapUser exampleLdapUser = new ExampleLdapUser();
    exampleLdapUser.setCustomUserField(customUserField);
    return exampleLdapUser;
  }
}
