package com.prosilion.presto.model.entity;

import com.prosilion.presto.model.dto.ExampleAzureUserDto;
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
public class ExampleAzureUser extends AppUser {
  private String customUserField;

  @Override
  public ExampleAzureUser getInstantiatedCustomAppUserType() {
    return this;
  }

  @Override
  public ExampleAzureUser createNewCustomAppUserInstance() {
    return new ExampleAzureUser();
  }

  public ExampleAzureUserDto convertToDto() throws InvocationTargetException, IllegalAccessException {
    ExampleAzureUserDto exampleAzureUserDto = new ExampleAzureUserDto();
    BeanUtils.copyProperties(exampleAzureUserDto, this);
    return exampleAzureUserDto;
  }
}
