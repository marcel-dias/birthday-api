package com.marceldias.birthday;

import com.marceldias.birthday.hello.HelloService;
import com.marceldias.birthday.user.User;
import com.marceldias.birthday.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("mysql")
public class BirthdayMessageApplicationTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer<>("mysql:8.0.20").withDatabaseName("birthday");

    @Autowired
    private UserRepository userRepository;

    private String baseUrl = "http://localhost:";
    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + port + "/hello";
    }

    @AfterEach
    public void afterSetup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateBirthday() {
        User realUser = new User("realUser", LocalDate.now().minusYears(30).plusDays(15));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> body = new HttpEntity<>("{\"dateOfBirth\": \"" + realUser.getDateOfBirth() + "\"}",
                headers);

        restTemplate.put(baseUrl + "/" + realUser.getName(), body);
        assertThat(userRepository.count(), equalTo(1L));
    }

    @Test
    void shouldGetBirthdayMessage() {
        String username = "barcelona";
        LocalDate birthday = LocalDate.now().minusYears(30);
        User barcelona = new User(username, birthday);

        userRepository.insert(barcelona);

        String message = restTemplate.getForObject(baseUrl + "/" + barcelona.getName(), String.class);
        assertThat(message, containsString(username));
        assertThat(message, containsString(String.format(HelloService.TODAYS_BIRTHDAY_MESSAGE, username)));
    }

    @Test
    void shouldGetBirthdayFutureMessage() {
        String username = "futureUser";
        Long days = 20L;
        LocalDate birthday = LocalDate.now().minusYears(30).plusDays(days);
        User barcelona = new User(username, birthday);

        userRepository.insert(barcelona);

        String message = restTemplate.getForObject(baseUrl + "/" + barcelona.getName(), String.class);
        assertThat(message, containsString(username));
        assertThat(message, containsString(String.format(HelloService.FUTURE_BIRTHDAY_MESSAGE, username, days)));
    }

    @Test
    void shouldGetBirthdayCelebratedMessage() {
        String username = "celebrated";
        Long days = 15L;
        LocalDate birthday = LocalDate.now().minusYears(30).minusDays(days);
        User barcelona = new User(username, birthday);

        userRepository.insert(barcelona);

        String message = restTemplate.getForObject(baseUrl + "/" + barcelona.getName(), String.class);
        assertThat(message, containsString(username));
        assertThat(message, containsString(String.format(HelloService.PAST_BIRTHDAY_MESSAGE, username, days)));
    }


}
