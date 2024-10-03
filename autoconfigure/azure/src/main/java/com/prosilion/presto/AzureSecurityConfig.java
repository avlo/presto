package com.prosilion.presto;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer;
import com.prosilion.presto.azure.authentication.AzureUserLocalAuthorities;
import com.prosilion.presto.azure.service.AzureAuthUserServiceImpl;
import com.prosilion.presto.azure.service.AzureUserDetailServiceImpl;
import com.prosilion.presto.security.AppUserLocalAuthorities;
import com.prosilion.presto.security.service.AuthUserDetailServiceImpl;
import com.prosilion.presto.security.service.AuthUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import javax.sql.DataSource;

@Slf4j
@AutoConfiguration
@EnableMethodSecurity
@ConditionalOnClass(AadWebApplicationHttpSecurityConfigurer.class)
@Import({
	DataSourceAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class })
@AutoConfigureBefore({JpaSecurityConfig.class, SecurityCoreConfig.class})
@EntityScan(basePackages = "com.prosilion.presto.azure.entity")
public class AzureSecurityConfig {

	@Bean
	public AadWebApplicationHttpSecurityConfigurer aadWebApplicationHttpSecurityConfigurer(HttpSecurity http) {
		return AadWebApplicationHttpSecurityConfigurer.aadWebApplication();
	}

	@Bean
	@DependsOn("mvc")
	public SecurityFilterChain filterChain(
		HttpSecurity http,
		MvcRequestMatcher.Builder mvc,
		AzureUserLocalAuthorities azureUserLocalAuthorities,
		AzureAuthUserServiceImpl azureAuthUserServiceImpl,
		AadWebApplicationHttpSecurityConfigurer aadWebApplicationHttpSecurityConfigurer
	) throws Exception {
		log.info("Loading ADOauth2 - Endpoint authorization configuration");
		http.apply(aadWebApplicationHttpSecurityConfigurer)
			.and()
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(mvc.pattern("/css/**")).permitAll()
					.requestMatchers(mvc.pattern("/images/**")).permitAll()
					.requestMatchers(mvc.pattern("/login")).permitAll()
					.requestMatchers(mvc.pattern("/login/oauth2/code/aad/")).permitAll()
					.requestMatchers(mvc.pattern("/aad")).permitAll()
					.requestMatchers(mvc.pattern("/**")).permitAll()
//						.access(azureUserLocalAuthorities)  <--- reintroduce
			)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userinfo -> userinfo
					.oidcUserService(azureAuthUserServiceImpl))
				.defaultSuccessUrl("/users", true)
				.permitAll()
			)
			.logout(logout -> logout
				.logoutRequestMatcher(mvc.pattern("/logout")).permitAll()
				.logoutSuccessUrl("/login")
			);
		return http.build();
	}

	@Bean
	@Primary
	public AuthUserDetailsService authUserDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
		return new AzureUserDetailServiceImpl(new AuthUserDetailServiceImpl(dataSource, passwordEncoder));
	}

	@Bean
	public AzureUserLocalAuthorities azureUserLocalAuthorities(AppUserLocalAuthorities appUserLocalAuthorities) {
		return new AzureUserLocalAuthorities(appUserLocalAuthorities);
	}
}
