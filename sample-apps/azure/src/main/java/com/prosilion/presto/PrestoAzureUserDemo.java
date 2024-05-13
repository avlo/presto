package com.prosilion.presto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PrestoAzureUserDemo extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PrestoAzureUserDemo.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(PrestoAzureUserDemo.class, args);
  }
}
