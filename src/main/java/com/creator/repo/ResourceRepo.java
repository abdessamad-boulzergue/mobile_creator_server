package com.apos.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apos.models.Resource;

public interface  ResourceRepo extends JpaRepository<Resource, Long> {
	
	@Query("FROM Resource r WHERE r.id = :id")
	public Resource get(@Param("id") Long id );
}
