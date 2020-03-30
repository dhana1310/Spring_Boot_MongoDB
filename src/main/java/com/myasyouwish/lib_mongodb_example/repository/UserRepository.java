package com.myasyouwish.lib_mongodb_example.repository;

import com.myasyouwish.lib_mongodb_example.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{company:{ $regex: ?0, $options: 'i' }}")
    List<User> fetchUserByCompany(String company);

    @Query("{name: { $regex: ?0, $options: 'i' }}")     //$regex contains the string, $options = 'i' searches case insensitive
    List<User> findUserByMatchingName(String name);

    List<User> findByCompanyContainingIgnoreCase(String company);
}
