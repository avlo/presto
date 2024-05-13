package com.prosilion.presto.config;

import com.prosilion.presto.repository.ExampleAzureUserRepository;
import com.prosilion.presto.model.entity.ExampleAzureUser;
import com.prosilion.presto.security.service.CustomizableAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {ExampleAzureUserRepository.class})
@EntityScan(basePackageClasses = {ExampleAzureUser.class})
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:application.yml")
})
public class ExampleAzureUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleAzureUserConfig.class);

	@Bean
	public CustomizableAppUserService customizableAppUserService() {
		LOGGER.info("EXAMPLE USER CONFIG - Creating ExampleAzureUser");
		return new CustomizableAppUserService(new ExampleAzureUser());
	}
}
