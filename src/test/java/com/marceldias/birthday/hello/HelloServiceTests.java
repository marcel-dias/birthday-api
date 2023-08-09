package com.marceldias.birthday.hello;

import com.marceldias.birthday.user.User;
import com.marceldias.birthday.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
public class HelloServiceTests {

    @TestConfiguration
    static class HelloServiceImplTestContextConfiguration {
        @Bean
        public HelloService helloService() {
            return new HelloService();
        }
    }

    @Autowired
    private HelloService helloService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User alex = new User("alex", LocalDate.now().plusDays(12));
        Mockito.when(userRepository.findByName(alex.getName())).thenReturn(alex);
    }

    @Test
    void testGetHelloMessage() {
        String username = "alex";
        HelloMessage message = helloService.getHelloMessage(username);
        assertThat(message.message(), equalTo("Hello, alex! Your birthday is in 12 day(s)"));
    }
}
