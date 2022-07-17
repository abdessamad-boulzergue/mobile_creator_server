package com.creator.repo;

import com.creator.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	@Query("FROM User u WHERE u.id = :id ")
	public User get(@Param("id")Long id);
	
	@Query("FROM User u WHERE u.name = :name and u.password = :password")
	public User getByCredentials(@Param("name")String id, @Param("password")String password);
}
