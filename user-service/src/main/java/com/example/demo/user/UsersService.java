package com.example.demo.user;

import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.logging.Logger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



@Service
public class UsersService {
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    private final UsersRepository usersRepository;

    @GetMapping
    public List<Users> getUsers()
    {
//        return List.of( new Users("mariam" , "ssssss" , "nona@yahooo" ,
//                LocalDate.now(), LocalDate.now()));
//        System.out.println( "nonna");
//
//        System.out.println( usersRepository.findAll());
//        Users mariam = new Users("mariam" , "ssssss" , "nona@yahooo" ,
//                LocalDate.now(), LocalDate.now());
//        usersRepository.save(mariam);
        usersRepository.findAll().forEach(System.out::println);
        return usersRepository.findAll();

    }

    public void addNewUser(Users user) {
//        String hashedPassword = passwordEncoder.encode(user.getPassword_hash());
//        user.setPassword_hash(hashedPassword);
        Optional<Users> usersOptional = usersRepository.findUsersByEmail(user.getEmail());
        if (usersOptional.isPresent()) {
            throw new IllegalStateException("email exists");
        } else {
            usersRepository.save(user);

        }
    }




//    public boolean authenticateUser(String username, String password) {
//        Users user = usersRepository.findByUsername(username);
//        if (user == null) {
//            return false;
//        }
//        // Hash the provided password
//        String hashedPassword = passwordEncoder.encode(password);
//        // Compare the hashed password with the password stored in the database
//        return passwordEncoder.matches(password, user.getPassword_hash());
//    }

    //System.out.println(user);   }

}
