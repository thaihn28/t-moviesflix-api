package com.example.tmovierestapi.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Firstname must not be empty")
    @Size( max = 20)
    private String firstName;

    @NotBlank(message = "Lastname must not be empty")
    @Size( max = 20)
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, max = 40)
    private String password;

    public String fullName(){
        return firstName + " " + lastName;
    }

}
