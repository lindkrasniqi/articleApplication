package com.example.articleProgram.controller;
import com.example.articleProgram.DTO.*;
import com.example.articleProgram.repositories.articleRepository;
import com.example.articleProgram.repositories.userRepository;
import com.example.articleProgram.services.articleService;
import com.example.articleProgram.sessionManager.SessionManagerFactory;
import com.example.articleProgram.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final com.example.articleProgram.services.userService userService;
    private final ModelMapper modelMapper;
    private final userRepository userRepository;
    private final articleService articleSerive;

    @PostMapping("/api/account/login")
    public ResponseEntity logInUser (@RequestBody UserLoginDTO userLoginDTO) {
        User x = userService.loginUser(userLoginDTO);
        if (x != null) {
            return ResponseEntity.ok().body(x);
        }else {
            return ResponseEntity.badRequest().header("error_message", "credentials are wrong").build();
        }
    }
    @PostMapping("/api/account/register")
    public ResponseEntity signUpUser (@RequestBody UserSignUpDTO userSignUpDTO, HttpServletResponse response) {
        UserSignUpDTO user = userService.saveUser(userSignUpDTO);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }else {
            Map<String , String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Username already taken");
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @PostMapping("/api/{username}")
    public ResponseEntity followUser (@PathVariable(name = "username") String username) {
        User user = userService.getUserByUsername(username);
        if (userService.followUser(getLoggedInUser(), user) != null) {
            return ResponseEntity.ok(userService.followUser(getLoggedInUser(), user));
        }else {
            Map<String, String> errorMessage = new HashMap<>();
            errorMessage.put("Error on request", "You already follow this user");
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @GetMapping("/api/{username}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(userService.getUserProfile(username));
    }
    @GetMapping("api/{username}/followings")
    public ResponseEntity<List<UserListDTO>> getFollowings (@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(userService.getFollowings(userService.getUserByUsername(username)));
    }

    @GetMapping("/api/{username}/followers")
    public ResponseEntity<List<UserListDTO>> getFollowers (@PathVariable(name = "username") String username) {
        return ResponseEntity.ok().body(userService.getFollowers(userService.getUserByUsername(username)));
    }

    @GetMapping("/api/feed")
    public ResponseEntity<List<GetArticlesDTO>> getFeedArticles () {
        return ResponseEntity.ok().body(articleSerive.getFeedArticles(getLoggedInUser().getId()));
    }
    private User getLoggedInUser () {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user= null;
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                user = u;
            }
        }
        return user;
    }

}
