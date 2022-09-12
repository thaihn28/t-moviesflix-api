package com.example.tmovierestapi.utils;

import com.example.tmovierestapi.email.EmailSender;
import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppUtils {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Value("${app.apiURL}")
    private String rootURL;

    @Value("${spring.mail.username}")
    private String adminEmail;

    public static void validatePageNumberAndSize(int pageNo, int pageSize) {
        if (pageNo < 0) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
        }

        if (pageSize < 0) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
        }

        if (pageSize > Integer.parseInt(AppConstants.MAX_PAGE_SIZE)) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public void notifyNewMovie(MovieDTO movieDTO){
        try {
            List<User> userResponses = userRepository.findAll();
            for(User user : userResponses){
                if(!adminEmail.equals(user.getEmail())){
                    emailSender.notifyNewMovie(user.getEmail(), movieDTO.getName(), rootURL + "movies/" + movieDTO.getSlug());
                }
            }
        }catch (Exception e){
            throw new RuntimeException("Can not send email");
        }

    }
}
