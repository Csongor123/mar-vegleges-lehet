package com.example.sportesemenynyilvantartorendszer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SportesemenynyilvantartorendszerApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SportesemenynyilvantartorendszerApplication.class, args);
	}
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
}}
