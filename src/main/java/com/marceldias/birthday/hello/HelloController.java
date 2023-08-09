package com.marceldias.birthday.hello;

import com.marceldias.birthday.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/{username}")
    public ResponseEntity<HelloMessage> getBirthdayMessage(@PathVariable String username) {
        return ResponseEntity.ok(helloService.getHelloMessage(username));
    }

    @PutMapping("/{username}")
    @PostMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> savesUser(@PathVariable String username, @RequestBody(required = true) final User user) {
        user.setName(username);
        helloService.save(user);
        return ResponseEntity.noContent().build();
    }
}
