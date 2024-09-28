package com.prosilion.presto.model.entity;

import com.prosilion.presto.model.dto.ExampleNostrUserDto;
import com.prosilion.presto.security.entity.AppUser;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@Entity
public class ExampleNostrUser extends AppUser {
  private String pubKey;

  @Override
  public ExampleNostrUser getInstantiatedCustomAppUserType() {
    return this;
  }

  @Override
  public ExampleNostrUser createNewCustomAppUserInstance() {
    return new ExampleNostrUser();
  }

  public ExampleNostrUserDto convertToDto()
      throws InvocationTargetException, IllegalAccessException {
    ExampleNostrUserDto exampleNostrUserDto = new ExampleNostrUserDto();
    BeanUtils.copyProperties(exampleNostrUserDto, this);
    return exampleNostrUserDto;
  }
}
