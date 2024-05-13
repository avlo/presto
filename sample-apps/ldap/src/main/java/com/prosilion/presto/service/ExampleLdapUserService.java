package com.prosilion.presto.service;

import com.prosilion.presto.model.dto.ExampleLdapUserDto;
import com.prosilion.presto.model.entity.ExampleLdapUser;
import com.prosilion.presto.repository.ExampleLdapUserRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExampleLdapUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleLdapUserService.class);
	private final ExampleLdapUserRepository exampleLdapUserRepository;

	@Autowired
	public ExampleLdapUserService(ExampleLdapUserRepository exampleLdapUserRepository) {
		this.exampleLdapUserRepository = exampleLdapUserRepository;
	}

	public ExampleLdapUser findById(Long id) {
		return exampleLdapUserRepository.findById(id).get();
	}

	public ExampleLdapUser findById(@NonNull ExampleLdapUserDto exampleLdapUserDto) throws InvocationTargetException, IllegalAccessException {
		return findByExampleUser(exampleLdapUserDto.convertToExampleUser());
	}

	public ExampleLdapUser findByExampleUser(@NonNull ExampleLdapUser exampleLdapUser) {
		return Objects.isNull(exampleLdapUser.getId()) ? exampleLdapUser : findById(exampleLdapUser.getId());
	}

	// TODO: can repurpose below to use AppUserService instead?
	@Transactional
	public ExampleLdapUserDto update(@NonNull ExampleLdapUserDto exampleLdapUserDto)
			throws InvocationTargetException, IllegalAccessException {
		LOGGER.info("EXAMPLE USER - updating");
		ExampleLdapUser exampleLdapUser = exampleLdapUserDto.convertToExampleUser();
		ExampleLdapUser retrievedUser = findByExampleUser(exampleLdapUser);
		LOGGER.info("Confirm retrieved existing exampleLdapUser [{}]", retrievedUser);
		ExampleLdapUser returnUser = exampleLdapUserRepository.save(exampleLdapUser);
		LOGGER.info("Updating exampleLdapUser [{}]", returnUser);
		return exampleLdapUserRepository.findById(exampleLdapUser.getId()).get().convertToDto();
	}
}
