package com.creator;

import com.creator.models.Resource;
import com.creator.models.ResourceType;
import com.creator.services.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CreatorServerApplication implements ApplicationRunner {

	@Autowired
	ResourcesService resourceService;

	public static void main(String[] args) {
		SpringApplication.run(CreatorServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		if(resourceService.getType(ResourceType.TYPE_DOCUMENT) == null) {
			resourceService.saveType(Resource.getResourceType(ResourceType.TYPE_DOCUMENT));
		}
	}
}
