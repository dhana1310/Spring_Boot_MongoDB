package com.myasyouwish.lib_mongodb_example.service;

import com.myasyouwish.lib_mongodb_example.dto.User;
import com.myasyouwish.lib_mongodb_example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MongoDBService {

    private UserRepository userRepository;

    public List<User> fetchAllUserDetail() {
        return userRepository.findAll();
    }

    public User fetchUserDetail(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public List<User> fetchUserDetailByCompany(String company) {
        return userRepository.fetchUserByCompany(company);
    }

    public List<User> findUserByMatchingName(String name) {
        return userRepository.findUserByMatchingName(name);
    }

    public List<User> findUserByMatchingCompany(String name) {
        return userRepository.findByCompanyContainingIgnoreCase(name);
    }

    public User saveUserDetails(User user) {
        return userRepository.save(user);
    }

    public User updateUserDetails(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
