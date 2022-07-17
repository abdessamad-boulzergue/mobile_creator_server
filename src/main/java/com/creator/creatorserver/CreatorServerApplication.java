package com.creator.creatorserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.creator")
public class CreatorServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreatorServerApplication.class, args);
	}

}
