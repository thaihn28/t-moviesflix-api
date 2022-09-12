package com.example.tmovierestapi.security.services;

import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = null;
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isPresent()){
                user = optionalUser.get();
            }
            if(user == null){
                User userByEmail = userRepository.findUserByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email:" + username));
                return CustomUserDetails.build(userByEmail);
            }
            return CustomUserDetails.build(user);
        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("User Not Found");
        }

    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

}
