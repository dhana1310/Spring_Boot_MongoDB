package com.myasyouwish.lib_mongodb_example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "collection_user")
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private Integer age;
    private String company;
}
