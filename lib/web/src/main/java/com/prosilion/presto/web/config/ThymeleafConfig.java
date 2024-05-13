package com.prosilion.presto.web.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {
  @Bean
  public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine springTemplateEngine){
    final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setViewNames(new String[] {"thymeleaf/*"});
    viewResolver.setExcludedViewNames(new String[] {"jsp/*"});
    viewResolver.setTemplateEngine(springTemplateEngine);
    viewResolver.setCharacterEncoding("UTF-8");
    return viewResolver;
  }

  @Bean
  public SpringResourceTemplateResolver htmlTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);

//    templateResolver.setPrefix("/WEB-INF/views/");
//    below altnerative also works
    templateResolver.setPrefix("classpath:/templates/");

    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);
    templateResolver.setOrder(0);
    return templateResolver;
  }
}
