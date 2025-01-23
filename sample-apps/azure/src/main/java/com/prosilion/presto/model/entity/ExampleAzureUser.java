package com.prosilion.presto.model.entity;

import com.prosilion.presto.model.dto.ExampleAzureUserDto;
import com.prosilion.presto.security.entity.AppUser;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  public ExampleAzureUserDto convertToDto() {
    ExampleAzureUserDto exampleAzureUserDto = new ExampleAzureUserDto();
    exampleAzureUserDto.setCustomUserField(customUserField);
    return exampleAzureUserDto;
  }
}
