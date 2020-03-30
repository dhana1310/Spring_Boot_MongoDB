package com.myasyouwish.lib_mongodb_example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myasyouwish.lib_mongodb_example.dto.User;
import com.myasyouwish.lib_mongodb_example.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class LibMongodbExampleApplicationTests {

    @Autowired
    UserRepository userRepository;

    @LocalServerPort
    private Integer port;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        RestAssured.port = port;
        userRepository.saveAll(data());
    }

    public List<User> data() {
        return Arrays.asList(new User(null, "Dhananjay", 25, "Cognizant"),
                new User(null, "Famia", 24, "Amazon"),
                new User("manId", "Mandeep", 25, "Cognizant"));
    }

    @After
    public void afterEachMethod() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json").get("/fetchAllUsers");

        List<User> allUserResponse = objectMapper.readValue(response.asString(), new TypeReference<List<User>>() {
        });
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals(allUserResponse.size(), 3);
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("name",
                Matchers.is("Dhananjay")), Matchers.hasProperty("name",
                Matchers.is("Famia"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("company",
                Matchers.is("Cognizant")), Matchers.hasProperty("company",
                Matchers.is("Amazon"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("age",
                Matchers.is(25)), Matchers.hasProperty("age",
                Matchers.is(24))));
    }

    @Test
    public void testGetSpecificUser() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json").get("/fetchUser/{userId}", "manId");

        User userResponse = objectMapper.readValue(response.asString(), User.class);
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals("manId", userResponse.getId());
        Assert.assertEquals("Mandeep", userResponse.getName());
        Assert.assertEquals(Integer.valueOf(25), userResponse.getAge());
        Assert.assertEquals("Cognizant", userResponse.getCompany());
    }

    @Test
    public void testGetUserByCompanyName() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json").get("/fetchAllUsersByCompany/{company}", "Cognizant");

        List<User> allUserResponse = objectMapper.readValue(response.asString(), new TypeReference<List<User>>() {
        });
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals(allUserResponse.size(), 2);
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("name",
                Matchers.is("Dhananjay")), Matchers.hasProperty("name",
                Matchers.is("Mandeep"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("company",
                Matchers.is("Cognizant"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("age",
                Matchers.is(25))));
    }

    @Test
    public void testGetUserByUserName() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json").get("/fetchAllUsersByName/{name}", "Famia");

        List<User> allUserResponse = objectMapper.readValue(response.asString(), new TypeReference<List<User>>() {
        });
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals(allUserResponse.size(), 1);
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("name",
                Matchers.is("Famia"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("company",
                Matchers.is("Amazon"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("age",
                Matchers.is(24))));
    }

    @Test
    public void testGetUserByPartialCompanyName() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json").get("/fetchAllUsersByCompanyMatching/{company}", "amaz");

        List<User> allUserResponse = objectMapper.readValue(response.asString(), new TypeReference<List<User>>() {
        });
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals(allUserResponse.size(), 1);
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("name",
                Matchers.is("Famia"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("company",
                Matchers.is("Amazon"))));
        Assert.assertThat(allUserResponse, Matchers.hasItems(Matchers.hasProperty("age",
                Matchers.is(24))));
    }

    @Test
    public void testSaveUser() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json")
                .body(new User(null, "Ranjan", 27, "Yardi")).post("/saveUser");

        List<User> savedUserList = userRepository.findUserByMatchingName("Ranjan");
        savedUserList.stream().findFirst().ifPresentOrElse(user -> {
            Assert.assertEquals(HttpStatus.CREATED.value(), response.statusCode());
            Assert.assertEquals("Ranjan", user.getName());
            Assert.assertEquals(Integer.valueOf(27), user.getAge());
            Assert.assertEquals("Yardi", user.getCompany());
        }, () -> {
            throw new AssertionError();
        });

        User userResponse = objectMapper.readValue(response.asString(), User.class);
        Assert.assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        Assert.assertEquals("Ranjan", userResponse.getName());
        Assert.assertEquals(Integer.valueOf(27), userResponse.getAge());
        Assert.assertEquals("Yardi", userResponse.getCompany());
    }

    @Test
    public void testUpdateUser() throws JsonProcessingException {

        Response response = RestAssured.given().contentType("application/json")
                .body(new User("manId", "Mandeep", 25, "Verizon")).patch("/updateUser");

        Optional<User> savedUserOptional = userRepository.findById("manId");
        savedUserOptional.ifPresentOrElse(user -> {
            Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
            Assert.assertEquals("Mandeep", user.getName());
            Assert.assertEquals(Integer.valueOf(25), user.getAge());
            Assert.assertEquals("Verizon", user.getCompany());
        }, () -> {
            throw new AssertionError();
        });

        User userResponse = objectMapper.readValue(response.asString(), User.class);
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals("Mandeep", userResponse.getName());
        Assert.assertEquals(Integer.valueOf(25), userResponse.getAge());
        Assert.assertEquals("Verizon", userResponse.getCompany());
    }

    @Test
    public void testDeleteUser() {

        Response response = RestAssured.given().contentType("application/json").delete("/deleteUser/{userId}", "manId");

        Optional<User> savedUserOptional = userRepository.findById("manId");

        Assert.assertTrue(savedUserOptional.isEmpty());
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assert.assertEquals("Deleted successfully", response.asString());
    }

}
