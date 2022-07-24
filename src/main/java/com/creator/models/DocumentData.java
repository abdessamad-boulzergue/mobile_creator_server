package com.creator.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class DocumentData {

    private String id;
    String content;
}
