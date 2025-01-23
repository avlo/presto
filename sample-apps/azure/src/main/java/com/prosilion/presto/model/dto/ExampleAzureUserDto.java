package com.prosilion.presto.model.dto;

import com.prosilion.presto.model.entity.ExampleAzureUser;
import com.prosilion.presto.web.model.AppUserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ExampleAzureUserDto extends AppUserDto {
  private String customUserField;
  public ExampleAzureUser convertToExampleUser() {
    ExampleAzureUser exampleAzureUser = new ExampleAzureUser();
    exampleAzureUser.setCustomUserField(customUserField);
    return exampleAzureUser;
  }
}
