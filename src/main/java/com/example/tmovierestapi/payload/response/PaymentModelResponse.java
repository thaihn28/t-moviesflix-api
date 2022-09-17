package com.example.tmovierestapi.payload.response;

import com.example.tmovierestapi.payload.dto.UserDTO;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@Data
public class PaymentModelResponse {
    @NotBlank
    private Long id;

    @NotBlank
    private String status;

    @NotBlank
    private String address;

    @DecimalMin(value = "0.0")
    private Double price;

    private String currency;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private PaymentMovieResponse movie;

    @NotBlank
    private UserDTO user;
}
