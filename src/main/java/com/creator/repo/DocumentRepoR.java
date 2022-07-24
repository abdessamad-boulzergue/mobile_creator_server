package com.creator.repo;

import com.creator.models.DocumentData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepoR extends MongoRepository<DocumentData, String> {

}