package com.example.articleProgram.services;

import com.example.articleProgram.DTO.*;
import com.example.articleProgram.model.Article;
import com.example.articleProgram.model.User;
import com.example.articleProgram.security.JWTDecoder;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class userService implements UserDetailsService {
    private final com.example.articleProgram.repositories.userRepository userRepository;
    private final com.example.articleProgram.repositories.articleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final roleService roleService;

    public userService(com.example.articleProgram.repositories.userRepository userRepository, com.example.articleProgram.repositories.articleRepository articleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, com.example.articleProgram.services.roleService roleService) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User loginUser(UserLoginDTO userLoginDTO) {
        User user = modelMapper.map(userLoginDTO, User.class);
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(user.getUsername())) {
                if (passwordEncoder.matches(userLoginDTO.getPassword(), u.getPassword())) {
                    return u;
                }else {
                    return null;
                }
            }
        }
        return null;
    }

    public UserSignUpDTO saveUser (UserSignUpDTO u) {
        User user = modelMapper.map(u, User.class);
        if (!usernameExists(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.getRoles().add(roleService.getRoleByName("USER"));
            userRepository.save(user);
            return modelMapper.map(user, UserSignUpDTO.class);
        }
        else {
            return null;
        }
    }

    public ListArticlesDTO publishArticle (PostArticleDTO postArticleDTO, User user) {
        Article a = modelMapper.map(postArticleDTO, Article.class);
        a.setDateTimePublished(LocalDateTime.now());
        a.setAuthor(user);
        articleRepository.save(a);
        ListArticlesDTO listArticlesDTO = modelMapper.map(a, ListArticlesDTO.class);
        return listArticlesDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                u.getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.getRole())));
                return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), authorities);
            }
        }
        throw new UsernameNotFoundException("Credentials are wrong");
    }
    private boolean usernameExists (String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return true ;
            }
        }
        return false;
    }
    public User getUserByUsername (String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    public UserProfileDTO getUserProfile (String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                UserProfileDTO userProfileDTO = modelMapper.map(u, UserProfileDTO.class);
                return userProfileDTO;
            }
        }
        return null;
    }

    public UserListDTO followUser (User user, User toFollow) {
        if (!followsUser(user, toFollow)) {
            user.getFollowing().add(toFollow);
            toFollow.getFollowersId().add(user.getId());
            userRepository.save(user);
            userRepository.save(toFollow);
            return modelMapper.map(toFollow, UserListDTO.class);
        }
        else {
            return null;
        }
    }

    public List<UserListDTO> getFollowings (User u) {
        return u.getFollowing().stream().map(user -> modelMapper.map(user, UserListDTO.class)).collect(Collectors.toList());
    }

    public List<UserListDTO> getFollowers (User u) {
        return u.getFollowers().stream().map(user -> modelMapper.map(user, UserListDTO.class)).collect(Collectors.toList());
    }
    public boolean followsUser (User user, User toFollow) {
        for(User u : user.getFollowing()) {
            if(u.getId().equals(toFollow.getId())) {
                return true;
            }
        }
        return false;
    }
}
