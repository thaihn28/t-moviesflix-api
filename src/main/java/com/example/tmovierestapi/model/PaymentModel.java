package com.example.tmovierestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "status")
    @NotBlank
    private String status;

    @Column(name = "address")
    @NotBlank
    private String address;

    @Column(name = "price")
    @DecimalMin(value = "0.0")
    private Double price;

    private String currency;

    @Column(name = "payment_method")
    @NotBlank
    private String paymentMethod;

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
