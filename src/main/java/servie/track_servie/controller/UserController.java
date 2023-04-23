package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servie.track_servie.entities.User;
import servie.track_servie.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user)
    {
        User savedUser = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("p1")
    public String p1()
    {
        return "PageA";
    }

    @GetMapping("p2")
    public String p2()
    {
        return "PageB";
    }
}