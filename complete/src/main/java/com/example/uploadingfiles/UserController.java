package com.example.uploadingfiles;

import com.example.uploadingfiles.model.User;
import com.example.uploadingfiles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) throws IOException {
          return userService.addUser(user);
    }

    @PostMapping("/checkAuth")
    public Map<String, String> checkAuth(@RequestBody User user){
        HashMap<String, String> map = new HashMap<>();

        if (userService.checkAuth(user)) map.put("message", "Пользователь с ником " + user.getUsername() + " зарегистрирован");
        else map.put("message", "Пользователь с ником " + user.getUsername() + " не зарегистрирован или пароль неверный");
        return map;
    }

    @GetMapping(value = "/admin/getAllUsers", produces = "application/json")
    public List<User> getUsers() throws IOException {
        return userService.getUsers();
    }
}