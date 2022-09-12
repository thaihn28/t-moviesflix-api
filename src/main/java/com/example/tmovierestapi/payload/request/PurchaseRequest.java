package com.example.tmovierestapi.payload.request;

import com.example.tmovierestapi.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PurchaseRequest {
    @NotBlank(message = "Movie ID is required")
    private Long movieID;
//    private User user;

}
