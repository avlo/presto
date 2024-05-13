package com.prosilion.presto.service;

import com.prosilion.presto.repository.ExampleJpaUserRepository;
import com.prosilion.presto.model.dto.ExampleJpaUserDto;
import com.prosilion.presto.model.entity.ExampleJpaUser;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Service
public class ExampleJpaUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleJpaUserService.class);
	private final ExampleJpaUserRepository exampleJpaUserRepository;

	@Autowired
	public ExampleJpaUserService(ExampleJpaUserRepository exampleJpaUserRepository) {
		this.exampleJpaUserRepository = exampleJpaUserRepository;
	}

	public ExampleJpaUser findById(Long id) {
		return exampleJpaUserRepository.findById(id).get();
	}

	public ExampleJpaUser findById(@NonNull ExampleJpaUserDto exampleJpaUserDto) throws InvocationTargetException, IllegalAccessException {
		return findByExampleUser(exampleJpaUserDto.convertToExampleUser());
	}

	public ExampleJpaUser findByExampleUser(@NonNull ExampleJpaUser exampleJpaUser) {
		return Objects.isNull(exampleJpaUser.getId()) ? exampleJpaUser : findById(exampleJpaUser.getId());
	}

	// TODO: can repurpose below to use AppUserService instead?
//	@Transactional
	public ExampleJpaUserDto update(@NonNull ExampleJpaUserDto exampleJpaUserDto)
			throws InvocationTargetException, IllegalAccessException {
		LOGGER.info("EXAMPLE USER - updating");
		ExampleJpaUser exampleJpaUser = exampleJpaUserDto.convertToExampleUser();
		ExampleJpaUser retrievedUser = findByExampleUser(exampleJpaUser);
		LOGGER.info("Confirm retrieved existing exampleJpaUser [{}]", retrievedUser);
		ExampleJpaUser returnUser = exampleJpaUserRepository.save(exampleJpaUser);
		LOGGER.info("Updating exampleJpaUser [{}]", returnUser);
		return exampleJpaUserRepository.findById(exampleJpaUser.getId()).get().convertToDto();
	}
}
