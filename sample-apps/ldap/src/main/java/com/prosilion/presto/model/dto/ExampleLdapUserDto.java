package com.prosilion.presto.model.dto;

import com.prosilion.presto.model.entity.ExampleLdapUser;
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
public class ExampleLdapUserDto extends AppUserDto {
	private String customUserField;
	public ExampleLdapUser convertToExampleUser() throws InvocationTargetException, IllegalAccessException {
		ExampleLdapUser exampleLdapUser = new ExampleLdapUser();
		BeanUtils.copyProperties(exampleLdapUser, this);
		return exampleLdapUser;
	}
}
