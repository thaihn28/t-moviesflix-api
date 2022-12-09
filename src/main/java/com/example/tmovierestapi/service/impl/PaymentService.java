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
import com.example.tmovierestapi.utils.AppGetLoggedIn;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        Authentication authentication = AppGetLoggedIn.getLoggedIn();
        User user = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = userRepository.findByUsername(currentUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("User " + currentUserName + " is not found!"));
            
        }
        Page<PaymentModel> paymentResponse = paymentRepository.findAllByUser(user, pageable);

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

            UserDTO userResponse = new UserDTO();
            userResponse.setId(user.getId());
            userResponse.setEmail(user.getEmail());
            userResponse.setFullName(user.fullName());
            userResponse.setUsername(user.getUsername());

            Movie m = p.getMovie();
            PaymentMovieResponse movieResponse = new PaymentMovieResponse(
                    m.getId(),
                    m.getImdbID(),
                    m.getName(),
                    m.getOriginName(),
                    m.getContent(),
                    m.getType(),
                    m.getThumbURL(),
                    m.getTrailerURL(),
                    m.getTime(),
                    m.getQuality(),
                    m.getSlug(),
                    m.getPosterURL(),
                    m.getYear(),
                    m.getIsHot(),
                    false,
                    0d,
                    m.getCreatedDate(),
                    m.getModifiedDate(),
                    m.getCountry(),
                    m.getActors(),
                    m.getDirectors(),
                    m.getCategories(),
                    m.getEpisodes(),
                    m.getComments()
            );
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
    public String addPayment(Payment payment) {
        PaymentModel paymentModel = new PaymentModel();
        Movie movie = null;

        for (Transaction transaction : payment.getTransactions()) {
            Long movieID = Long.valueOf(transaction.getPurchaseUnitReferenceId());
            movie = movieRepository.findMovieById(movieID)
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
            paymentModel.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(paymentModel);
        }
        String url = "movie/detail/" + movie.getSlug();

        return url;
    }

    @Override
    public void deletePayment(Movie movie) {
        List<PaymentModel> paymentModels = paymentRepository.findPaymentsModelByMovie(movie)
                        .orElseThrow(() -> new ResourceNotFoundException("List payment", "movie-" + movie.getName(), " not found"));

        paymentRepository.deleteAll(paymentModels);
    }


}
