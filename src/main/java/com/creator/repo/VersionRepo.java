package com.creator.repo;

import com.creator.models.ResourceVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface  VersionRepo extends JpaRepository<ResourceVersion, Long> {
	
	@Query("FROM ResourceVersion r WHERE r.id = :id")
	public ResourceVersion get(@Param("id") Long id );
	
	@Query(value="SELECT * FROM resource_version rv where rv.resource_id = :id order by rv.id desc limit 1 ", nativeQuery= true)
	public ResourceVersion getResourceMaxVersion(@Param("id") Long id );

	@Query("FROM ResourceVersion r WHERE r.resource.id = :id")
	public List<ResourceVersion> getResourceVersions(@Param("id") Long resourceId);
	
	
}
