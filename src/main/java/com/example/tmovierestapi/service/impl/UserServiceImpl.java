package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.model.Role;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.UserResponse;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.security.services.UserDetailsImpl;
import com.example.tmovierestapi.service.IUserService;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
import com.example.tmovierestapi.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public PagedResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sortOj = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOj);
        Page<User> userPage = userRepository.findAll(pageable);

        List<User> users = userPage.getNumberOfElements() == 0 ? Collections.emptyList() : userPage.getContent();
        List<UserResponse> contents = new ArrayList<>();

        for(User user : users){
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setEmail(user.getEmail());
            userResponse.setUsername(user.getUsername());
            userResponse.setFullName(user.fullName());
            userResponse.setRoles(user.getRoles());

            contents.add(userResponse);
        }

        return new PagedResponse<>(contents, userPage.getNumber(), userPage.getSize(), userPage.getTotalElements(),
                userPage.getTotalPages(), userPage.isLast());
    }

    @Override
    public void deleteUserByID(Long id) throws Exception {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID" + id));
        UserDetailsImpl userDetails = (UserDetailsImpl) AppGetLoggedIn.getLoggedIn().getPrincipal();

        if(user.getId() != userDetails.getId()){
            for(Role role : user.getRoles()){
                user.removeRole(role);
            }
            userRepository.delete(user);
        }else {
            throw new Exception("Can not delete currently logged-in user ");
        }
    }

    @Override
    public UserResponse getUserByID(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID" + id));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.fullName());
        userResponse.setRoles(user.getRoles());
        return userResponse;
    }
}
