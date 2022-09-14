package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.PaymentModel;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.dto.UserDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentModelResponse;
import com.example.tmovierestapi.payload.response.PaymentMovieResponse;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.repository.PaymentRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.service.IPaymentService;
import com.example.tmovierestapi.utils.AppUtils;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PaymentService implements IPaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagedResponse<PaymentModelResponse> getAllPayments(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PaymentModel> paymentResponse = paymentRepository.findAll(pageable);

        List<PaymentModel> contents = paymentResponse.getTotalElements() == 0 ? Collections.emptyList() : paymentResponse.getContent();

        List<PaymentModelResponse> paymentModelResponses = new ArrayList<>();
        for (PaymentModel p : contents){
            PaymentModelResponse response = new PaymentModelResponse();

            response.setId(p.getId());
            response.setStatus(p.getStatus());
            response.setAddress(p.getAddress());
            response.setPrice(p.getPrice());
            p.setCurrency(p.getCurrency());
            p.setPaymentMethod(p.getPaymentMethod());

            User user = p.getUser();
            UserDTO userResponse = new UserDTO();
            userResponse.setId(user.getId());
            userResponse.setEmail(user.getEmail());
            userResponse.setFullName(user.fullName());
            userResponse.setUsername(user.getUsername());

            PaymentMovieResponse movieResponse = modelMapper.map(p.getMovie(), PaymentMovieResponse.class);
            movieResponse.setIsPremium(false);
            movieResponse.setPrice(0d);
            response.setMovie(movieResponse);
            response.setUser(userResponse);

            paymentModelResponses.add(response);
        }
        return new PagedResponse<>(paymentModelResponses, paymentResponse.getNumber(), paymentResponse.getSize(),
                paymentResponse.getTotalElements(), paymentResponse.getTotalPages(), paymentResponse.isLast());
    }

    @Override
    public PaymentModel addPayment(Payment payment) {
        PaymentModel paymentModel = new PaymentModel();

        for (Transaction transaction : payment.getTransactions()) {
            Long movieID = Long.valueOf(transaction.getPurchaseUnitReferenceId());
            Movie movie = movieRepository.findMovieById(movieID)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", movieID));

            String payerEmail = payment.getPayer().getPayerInfo().getEmail();
            User user = userRepository.findUserByEmail(payerEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", payerEmail));

            paymentModel.setMovie(movie);
            paymentModel.setUser(user);

            Payer payer = payment.getPayer();

            paymentModel.setPaymentMethod(payer.getPaymentMethod());
            paymentModel.setStatus(payment.getState());
            paymentModel.setAddress(payer
                    .getPayerInfo()
                    .getShippingAddress()
                    .getCity()
            );
            Amount amount = transaction.getAmount();
            paymentModel.setPrice(Double.parseDouble(amount.getTotal()));
            paymentModel.setCurrency(amount.getCurrency());

            paymentRepository.save(paymentModel);
        }
        /*TODO: Add to Purchase table
         * paymentResponse.getPayer().getStatus() -> VERIFIED
         * paymentResponse.getPayer().getPaymentMethod() -> paypal
         * paymentResponse.getState() -> Approved
         *
         * paymentResponse.getPayer().getPayerInfo().getEmail() -> Email
         * paymentResponse.getPayer().getPayerInfo().getShippingAddress() -> Name, Address
         * transaction.getAmount.getTotal -> Price -> Transaction
         * transaction.getCurrency.getTotal -> Price -> Transaction
         */
        return paymentModel;
    }

}
