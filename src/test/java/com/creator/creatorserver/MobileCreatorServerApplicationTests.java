package com.creator.creatorserver;

import com.creator.models.Resource;
import com.creator.models.ResourceType;
import com.creator.services.ResourcesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@PropertySource("application-test.properties")
@SpringBootTest
class MobileCreatorServerApplicationDomainTests {

	@Autowired
	ResourcesService resourcesService;

	@BeforeAll
	public static void setUp() throws IOException {
		Path path = Paths.get("src", "test/resources/docs");
		System.setProperty("project.resources",path.toString());
	}


	@Test
	public void testResourceService()  {
		Resource resource = new Resource();
		ResourceType resourceType = new ResourceType(ResourceType.TYPES.DOCUMENT);
		resource.setType(resourceType);
		resource.setName("DOC");

		Resource savedResource = resourcesService.saveResource(resource);
		assertEquals(resource.getName(),savedResource.getName());
		assertEquals(resource.getType(),savedResource.getType());
		assertNotNull(savedResource.getId());
		assertTrue(savedResource.getId()>0);

		Resource retrievedResource = resourcesService.get(savedResource.getId());
		assertEquals(resource.getName(),retrievedResource.getName());
		assertEquals(resource.getType(),retrievedResource.getType());
	}

}
