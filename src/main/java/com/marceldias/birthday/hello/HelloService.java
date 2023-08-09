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

    private static final String futureBirthdayMessage = "Hello, %s! Your birthday is in %d day(s)";
    private static final String pastBirthdayMessage = "Hello, %s! Your birthday was %d day(s) ago";
    private static final String todaysBirthdayMessage = "Hello, %s! Happy birthday!";

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
            return new HelloMessage(String.format(futureBirthdayMessage, user.getName(),
                    ChronoUnit.DAYS.between(today, birthday)));
        } else if (birthday.isBefore(today)) {
            return new HelloMessage(String.format(pastBirthdayMessage, user.getName(),
                    ChronoUnit.DAYS.between(birthday, today)));
        }
        return new HelloMessage(String.format(todaysBirthdayMessage, user.getName()));
    }
}
