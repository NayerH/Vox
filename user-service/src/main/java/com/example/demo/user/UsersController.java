package com.example.demo.user;

import com.example.demo.auth.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<Users> getUsers()
    {
       return usersService.getUsers();
//        return List.of( new Users("mariam" , "ssssss" , "nona@yahooo" ,
//                LocalDate.now(), LocalDate.now()));


    }
    @PostMapping("/register")
    public void registerNewUser(@RequestBody Users user){
        usersService.addNewUser(user);

    }

//    @PostMapping("/login")
//    public void authenticateUser(String username, String password){
//        usersService.authenticateUser(username,password);
//    }
}
