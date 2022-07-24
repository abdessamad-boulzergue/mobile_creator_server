package com.creator.repo;

import com.creator.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface  ResourceRepo extends JpaRepository<Resource, Long> {
	
	@Query("FROM Resource r WHERE r.id = :id")
	public Resource get(@Param("id") Long id );
}
