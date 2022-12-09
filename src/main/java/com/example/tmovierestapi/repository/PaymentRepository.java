package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.PaymentModel;
import com.example.tmovierestapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    Optional<PaymentModel> getPaymentModelByUserIdAndMovie(Long userID, Movie movie);
    Optional<List<PaymentModel>> findPaymentsModelByMovie(Movie movie);
    Page<PaymentModel> findAllByUser(User user, Pageable pageable);
}
