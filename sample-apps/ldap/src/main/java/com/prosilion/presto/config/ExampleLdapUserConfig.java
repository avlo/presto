package com.prosilion.presto.config;

import com.prosilion.presto.repository.ExampleLdapUserRepository;
import com.prosilion.presto.model.entity.ExampleLdapUser;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {ExampleLdapUserRepository.class})
@EntityScan(basePackageClasses = {ExampleLdapUser.class})
public class ExampleLdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleLdapUserConfig.class);

	@Bean
	public CustomizableAppUserService customizableAppUserService() {
		LOGGER.info("EXAMPLE USER CONFIG - Creating ExampleLdapUser");
		return new CustomizableAppUserService(new ExampleLdapUser());
	}
}
