package com.prosilion.presto.model.dto;

import com.prosilion.presto.model.entity.ExampleJpaUser;
import com.prosilion.presto.web.model.AppUserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ExampleJpaUserDto extends AppUserDto {
  private String customUserField;
  private Date customDate;
  public ExampleJpaUser convertToExampleUser() {
    ExampleJpaUser exampleJpaUser = new ExampleJpaUser();
    exampleJpaUser.setCustomUserField(customUserField);
    exampleJpaUser.setCustomDate(customDate);
    return exampleJpaUser;
  }
}
