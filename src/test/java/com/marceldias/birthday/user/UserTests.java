package com.marceldias.birthday.user;


import com.marceldias.birthday.exceptions.NotValidException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {

    @Test
    void testValidUserName() {
        String username = "validUserName";
        User user = new User(username, LocalDate.of(1990,1,1));
        user.validate();
        assertThat(user.getName(), equalTo(username));
    }

    @Test
    void testInvalidUserName() {
        String username = "inv@lidUserName3";
        User user = new User(username, LocalDate.of(1990,1,2));

        Exception exception = assertThrows(NotValidException.class, () -> {
            user.validate();
        });

        String expectedMessage = "Username must contain only letters";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidBirthday() {
        String username = "myuser";
        LocalDate today = LocalDate.now();
        User user = new User(username, today);

        Exception exception = assertThrows(NotValidException.class, () -> {
            user.validate();
        });

        String expectedMessage = today.format(DateTimeFormatter.ISO_DATE)+" must be a date before the today date";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
