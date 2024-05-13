package com.prosilion.presto.model.dto;

import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.model.entity.ExampleJpaUser;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ExampleJpaUserDto extends AppUserDto {
	private String customUserField;
	private Date customDate;
	public ExampleJpaUser convertToExampleUser() throws InvocationTargetException, IllegalAccessException {
		ExampleJpaUser exampleJpaUser = new ExampleJpaUser();
		BeanUtils.copyProperties(exampleJpaUser, this);
		return exampleJpaUser;
	}
}
