package com.prosilion.presto.model.entity;

import com.prosilion.presto.model.dto.ExampleJpaUserDto;
import com.prosilion.presto.security.entity.AppUser;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class ExampleJpaUser extends AppUser {
  private String customUserField;

  @Temporal(TemporalType.TIMESTAMP)
  private Date customDate;

  @Override
  public ExampleJpaUser getInstantiatedCustomAppUserType() {
    return this;
  }

  @Override
  public ExampleJpaUser createNewCustomAppUserInstance() {
    return new ExampleJpaUser();
  }

  public ExampleJpaUserDto convertToDto()
      throws InvocationTargetException, IllegalAccessException {
    ExampleJpaUserDto exampleJpaUserDto = new ExampleJpaUserDto();
    BeanUtils.copyProperties(exampleJpaUserDto, this);
    return exampleJpaUserDto;
  }
}
