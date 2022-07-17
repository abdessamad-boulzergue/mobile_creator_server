package com.apos.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apos.models.ResourceType;

public interface ResourceTypeRepo extends JpaRepository<ResourceType, Long> {

	@Query("FROM ResourceType u WHERE u.id = :id ")
	public ResourceType get(@Param("id")Long id);

	@Query("FROM ResourceType u WHERE u.name = :name ")
	public ResourceType get(@Param("name")String typeName);
	
}
