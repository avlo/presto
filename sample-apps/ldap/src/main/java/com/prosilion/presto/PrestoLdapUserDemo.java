package com.prosilion.presto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PrestoLdapUserDemo extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PrestoLdapUserDemo.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(PrestoLdapUserDemo.class, args);
  }
}

