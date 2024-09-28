package com.prosilion.presto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PrestoNostrUserDemo extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PrestoNostrUserDemo.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(PrestoNostrUserDemo.class, args);
  }
}

