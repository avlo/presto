package com.prosilion.presto.service;

import com.prosilion.presto.repository.ExampleAzureUserRepository;
import com.prosilion.presto.model.dto.ExampleAzureUserDto;
import com.prosilion.presto.model.entity.ExampleAzureUser;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExampleAzureUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleAzureUserService.class);
	private final ExampleAzureUserRepository exampleAzureUserRepository;

	@Autowired
	public ExampleAzureUserService(ExampleAzureUserRepository exampleAzureUserRepository) {
		this.exampleAzureUserRepository = exampleAzureUserRepository;
	}

	public ExampleAzureUser findById(Long id) {
		return exampleAzureUserRepository.findById(id).get();
	}

	public ExampleAzureUser findById(@NonNull ExampleAzureUserDto exampleAzureUserDto) throws InvocationTargetException, IllegalAccessException {
		return findByExampleUser(exampleAzureUserDto.convertToExampleUser());
	}

	public ExampleAzureUser findByExampleUser(@NonNull ExampleAzureUser exampleAzureUser) {
		return Objects.isNull(exampleAzureUser.getId()) ? exampleAzureUser : findById(exampleAzureUser.getId());
	}

	// TODO: can repurpose below to use AppUserService instead?
	@Transactional
	public ExampleAzureUserDto update(@NonNull ExampleAzureUserDto exampleAzureUserDto)
			throws InvocationTargetException, IllegalAccessException {
		LOGGER.info("EXAMPLE USER - updating");
		ExampleAzureUser exampleAzureUser = exampleAzureUserDto.convertToExampleUser();
		ExampleAzureUser retrievedUser = findByExampleUser(exampleAzureUser);
		LOGGER.info("Confirm retrieved existing exampleAzureUser [{}]", retrievedUser);
		ExampleAzureUser returnUser = exampleAzureUserRepository.save(exampleAzureUser);
		LOGGER.info("Updating exampleAzureUser [{}]", returnUser);
		return exampleAzureUserRepository.findById(exampleAzureUser.getId()).get().convertToDto();
	}
}
