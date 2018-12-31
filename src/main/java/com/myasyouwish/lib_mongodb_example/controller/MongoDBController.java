package com.myasyouwish.lib_mongodb_example.controller;

import  com.myasyouwish.lib_mongodb_example.service.MongoDBService;
import  com.myasyouwish.lib_mongodb_example.dto.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class MongoDBController {

    private final MongoDBService mongoDBService;

    @GetMapping(value = "/fetchAllUsers")
    public ResponseEntity<List<User>> fetchAllUsers() {

        List<User> userList = mongoDBService.fetchAllUserDetail();
        return CollectionUtils.isEmpty(userList) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/fetchUser/{userId}")
    public ResponseEntity<User> fetchUserById(@PathVariable("userId") Integer userId) {

        return Optional.ofNullable(mongoDBService.fetchUserDetail(userId)).map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/fetchAllUsersByCompany/{company}")
    public ResponseEntity<List<User>> fetchAllUsersByCompany(@PathVariable("company") String company) {

        List<User> userList = mongoDBService.fetchUserDetailByCompany(company);
        return CollectionUtils.isEmpty(userList) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/fetchAllUsersByName/{name}")
    public ResponseEntity<List<User>> findUserByMatchingName(@PathVariable("name") String name) {

        List<User> userList = mongoDBService.findUserByMatchingName(name);
        return CollectionUtils.isEmpty(userList) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<User> saveUserData(@RequestBody User user) {

        return new ResponseEntity<>(mongoDBService.saveUserDetails(user), HttpStatus.CREATED);
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<User> updateUserData(@RequestBody User user) {

        return new ResponseEntity<>(mongoDBService.updateUserDetails(user), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Integer userId) {
        mongoDBService.deleteUser(userId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
