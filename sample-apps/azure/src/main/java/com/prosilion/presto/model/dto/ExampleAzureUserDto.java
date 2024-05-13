package com.prosilion.presto.model.dto;

import com.prosilion.presto.model.entity.ExampleAzureUser;
import com.prosilion.presto.web.model.AppUserDto;
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
public class ExampleAzureUserDto extends AppUserDto {
	private String customUserField;
	public ExampleAzureUser convertToExampleUser() throws InvocationTargetException, IllegalAccessException {
		ExampleAzureUser exampleAzureUser = new ExampleAzureUser();
		BeanUtils.copyProperties(exampleAzureUser, this);
		return exampleAzureUser;
	}
}
