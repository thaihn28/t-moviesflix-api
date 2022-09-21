package com.example.tmovierestapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PurchaseRequest {
    @NotBlank(message = "Movie ID must not be empty")
    private Long movieID;
//    private User user;

}
