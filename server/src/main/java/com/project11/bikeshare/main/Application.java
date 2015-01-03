package com.project11.bikeshare.main;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Import;



@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import(ScheduledTasks.class)
public class Application extends SpringBootServletInitializer{

	public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);
	}
	
	 @Bean
	    MultipartConfigElement multipartConfigElement() {
	        MultipartConfigFactory factory = new MultipartConfigFactory();
	        factory.setMaxFileSize("5120KB");
	        factory.setMaxRequestSize("5120KB");
	        return factory.createMultipartConfig();
	    }
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(applicationClass);
	    }

	    private static Class<Application> applicationClass = Application.class;
}
