package com.example.tmovierestapi.service.paypal;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.impl.PaymentService;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;

    private List<Transaction> transactions = new ArrayList<>();
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PaymentService paymentService;


    public String createPayment(
            Long movieID,
            String currency,
            String method,
            String intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        try {
            Movie movie = movieRepository.findMovieById(movieID)
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST , "Movie with ID" + movieID + " not found!"));
            Amount amount = new Amount();
            amount.setCurrency(currency);
            amount.setTotal(String.format("%.2f", movie.getPrice()));
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setPurchaseUnitReferenceId(movie.getId().toString());
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod(method);

            Payment payment = new Payment();
            payment.setIntent(intent);
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl);
            payment.setRedirectUrls(redirectUrls);

            Payment paymentResponse = payment.create(apiContext);

//            System.out.println(paymentResponse.toJSON());

            for (Links link : paymentResponse.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
           throw new PayPalRESTException(e.getMessage());
        }
        return null;

    }

    public Payment executePayment(String paymentId, String payerId, CustomUserDetails currentUser) throws PayPalRESTException {
        try {
            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setTransactions(transactions);
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerId);
            Payment paymentResponse = payment.execute(apiContext, paymentExecute);
            paymentResponse.getTransactions().stream().filter(item -> item.getPurchaseUnitReferenceId() != null)
                    .collect(Collectors.toList());
            paymentResponse.getPayer().getPayerInfo().setEmail(currentUser.getEmail());
            paymentResponse.setTransactions(payment.getTransactions());
            if (paymentResponse.getState().equals("approved")) {
                paymentService.addPayment(paymentResponse);
                return paymentResponse;
            }
        } catch (PayPalRESTException e) {
            throw new PayPalRESTException(e.getMessage());
        }
        return null;
    }

}
