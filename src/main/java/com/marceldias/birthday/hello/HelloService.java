package com.marceldias.birthday.hello;

import com.marceldias.birthday.exceptions.NotFoundException;
import com.marceldias.birthday.user.User;
import com.marceldias.birthday.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class HelloService {

    public static final String FUTURE_BIRTHDAY_MESSAGE = "Hello, %s! Your birthday is in %d day(s)";
    public static final String PAST_BIRTHDAY_MESSAGE = "Hello, %s! Your birthday was %d day(s) ago";
    public static final String TODAYS_BIRTHDAY_MESSAGE = "Hello, %s! Happy birthday!";

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        user.validate();
        userRepository.insert(user);
    }

    public HelloMessage getHelloMessage(String username) {
        User user = userRepository.findByName(username);
        return buildMessage(user);
    }

    public HelloMessage buildMessage(User user) {
        if (Objects.isNull(user)) {
            throw new NotFoundException("Username not found");
        }

        LocalDate today = LocalDate.now();
        final LocalDate birthday = user.getDateOfBirth().withYear(today.getYear());
        if (birthday.isAfter(today)) {
            return new HelloMessage(String.format(FUTURE_BIRTHDAY_MESSAGE, user.getName(),
                    ChronoUnit.DAYS.between(today, birthday)));
        } else if (birthday.isBefore(today)) {
            return new HelloMessage(String.format(PAST_BIRTHDAY_MESSAGE, user.getName(),
                    ChronoUnit.DAYS.between(birthday, today)));
        }
        return new HelloMessage(String.format(TODAYS_BIRTHDAY_MESSAGE, user.getName()));
    }
}
