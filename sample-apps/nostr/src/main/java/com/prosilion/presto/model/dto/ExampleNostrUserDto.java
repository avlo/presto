package com.prosilion.presto.model.dto;

import com.prosilion.presto.model.entity.ExampleNostrUser;
import com.prosilion.presto.web.model.AppUserDto;
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
public class ExampleNostrUserDto extends AppUserDto {
  private String pubKey;
  public ExampleNostrUser convertToExampleUser() throws InvocationTargetException, IllegalAccessException {
    ExampleNostrUser exampleNostrUser = new ExampleNostrUser();
    BeanUtils.copyProperties(exampleNostrUser, this);
    return exampleNostrUser;
  }
}
