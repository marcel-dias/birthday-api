package com.marceldias.birthday.hello;

import com.marceldias.birthday.exceptions.NotValidException;
import com.marceldias.birthday.user.User;
import com.marceldias.birthday.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User alex = new User("alex", LocalDate.now().plusDays(12));
        Mockito.when(userRepository.findByName(alex.getName())).thenReturn(alex);

        User celebrated = new User("celebrated", LocalDate.now().minusDays(15).minusYears(10));
        Mockito.when(userRepository.findByName(celebrated.getName())).thenReturn(celebrated);

        User happyperson = new User("happyperson", LocalDate.now().minusYears(15));
        Mockito.when(userRepository.findByName(happyperson.getName())).thenReturn(happyperson);
    }

    @Test
    void testGetBirthdayMessage() throws Exception {
        String username = "happyperson";
        this.mockMvc.perform(get("/hello/" + username))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Hello, " + username + "! Happy birthday!\"}"));
    }
    @Test
    void testGetBirthdayMessageExistingUsername() throws Exception {
        String username = "alex";
        this.mockMvc.perform(get("/hello/" + username))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Hello, " + username + "! Your birthday is in 12 day(s)\"}"));
    }

    @Test
    public void testGetBirthdayMessageAlreadyCelebratedThisYear() throws Exception {
        String username = "celebrated";
        this.mockMvc.perform(get("/hello/" + username))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Hello, " + username + "! Your birthday was 15 day(s) ago\"}"));
    }

    @Test
    public void testGetBirthdayMessageNonExistingUsername() throws Exception {
        String username = "missing";
        this.mockMvc.perform(get("/hello/" + username))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateValidBirthday() throws Exception {
        String username = "test";
        this.mockMvc.perform(
                        put("/hello/" + username).contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"dateOfBirth\": \"" + LocalDate.now().minusDays(12) + "\"}")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateFutureBirthday() throws Exception {
        String username = "future";
        LocalDate futureBirthday = LocalDate.now().plusDays(15);
        this.mockMvc.perform(
                        put("/hello/" + username).contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"dateOfBirth\": \"" + futureBirthday + "\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotValidException))
                .andExpect(result -> assertEquals(futureBirthday + " must be a date before the today date",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void testCreateTodayBirthday() throws Exception {
        String username = "today";
        LocalDate todayBirthday = LocalDate.now();
        this.mockMvc.perform(
                        put("/hello/" + username).contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"dateOfBirth\": \"" + todayBirthday + "\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotValidException))
                .andExpect(result -> assertEquals(todayBirthday + " must be a date before the today date",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void testCreateInvalidUsername() throws Exception {
        String username = "User4name";
        LocalDate todayBirthday = LocalDate.now().minusDays(150);
        this.mockMvc.perform(
                        put("/hello/" + username).contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"dateOfBirth\": \"" + todayBirthday + "\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotValidException))
                .andExpect(result -> assertEquals("Username must contain only letters",
                        result.getResolvedException().getMessage()));
    }
}
