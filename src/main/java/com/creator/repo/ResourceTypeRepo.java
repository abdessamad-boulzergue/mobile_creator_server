package com.creator.repo;

 import com.creator.models.ResourceType;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 import org.springframework.stereotype.Repository;

@Repository
public interface ResourceTypeRepo extends JpaRepository<ResourceType, Long> {

	@Query("FROM ResourceType u WHERE u.id = :id ")
	public ResourceType get(@Param("id")Long id);

	@Query("FROM ResourceType u WHERE u.name = :name ")
	public ResourceType get(@Param("name")String typeName);
	
}
