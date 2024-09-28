package com.prosilion.presto.service;

import com.prosilion.presto.model.dto.ExampleNostrUserDto;
import com.prosilion.presto.model.entity.ExampleNostrUser;
import com.prosilion.presto.repository.ExampleNostrUserRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Service
public class ExampleNostrUserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleNostrUserService.class);
  private final ExampleNostrUserRepository exampleNostrUserRepository;

  @Autowired
  public ExampleNostrUserService(ExampleNostrUserRepository exampleNostrUserRepository) {
    this.exampleNostrUserRepository = exampleNostrUserRepository;
  }

  public ExampleNostrUser findById(Long id) {
    return exampleNostrUserRepository.findById(id).get();
  }

  public ExampleNostrUser findById(@NonNull ExampleNostrUserDto exampleNostrUserDto) throws InvocationTargetException, IllegalAccessException {
    return findByExampleUser(exampleNostrUserDto.convertToExampleUser());
  }

  public ExampleNostrUser findByExampleUser(@NonNull ExampleNostrUser exampleNostrUser) {
    return Objects.isNull(exampleNostrUser.getId()) ? exampleNostrUser : findById(exampleNostrUser.getId());
  }

  // TODO: can repurpose below to use AppUserService instead?
  @Transactional
  public ExampleNostrUserDto update(@NonNull ExampleNostrUserDto exampleNostrUserDto)
      throws InvocationTargetException, IllegalAccessException {
    LOGGER.info("EXAMPLE USER - updating");
    ExampleNostrUser exampleNostrUser = exampleNostrUserDto.convertToExampleUser();
    ExampleNostrUser retrievedUser = findByExampleUser(exampleNostrUser);
    LOGGER.info("Confirm retrieved existing exampleNostrUser [{}]", retrievedUser);
    ExampleNostrUser returnUser = exampleNostrUserRepository.save(exampleNostrUser);
    LOGGER.info("Updating exampleNostrUser [{}]", returnUser);
    return exampleNostrUserRepository.findById(exampleNostrUser.getId()).get().convertToDto();
  }
}
