package com.creator.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GeneralConfigs {

	@Value("${plugin.datasource.location}")
	private String location;
	
	@Value("${project.resources}")
	private String resourcesPath;
	
	@Value("${actions.definitionPath}")
	private String actionsDefinitionPath;

	@Bean(name="workflow_connection")
	public JsonNode getPluginDatasource() throws JsonProcessingException, IOException  {
		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File(location);
		JsonNode node = objectMapper.readTree(file);
		return node;
	}
	
	public String getResourcesPath() {
		return this.resourcesPath;
	}
	public String getActionsDefinitionPath() {
		return this.actionsDefinitionPath;
	}
}
