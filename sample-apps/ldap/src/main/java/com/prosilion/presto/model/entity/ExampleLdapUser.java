package com.prosilion.presto.model.entity;

import com.prosilion.presto.model.dto.ExampleLdapUserDto;
import com.prosilion.presto.security.entity.AppUser;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import java.lang.reflect.InvocationTargetException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@Entity
public class ExampleLdapUser extends AppUser {
  private String customUserField;

  @Override
  public ExampleLdapUser getInstantiatedCustomAppUserType() {
    return this;
  }

  @Override
  public ExampleLdapUser createNewCustomAppUserInstance() {
    return new ExampleLdapUser();
  }

  public ExampleLdapUserDto convertToDto()
      throws InvocationTargetException, IllegalAccessException {
    ExampleLdapUserDto exampleLdapUserDto = new ExampleLdapUserDto();
    BeanUtils.copyProperties(exampleLdapUserDto, this);
    return exampleLdapUserDto;
  }
}
