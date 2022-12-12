package com.example.tmovierestapi.service.paypal;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.impl.PaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;

    private List<Transaction> transactionCopied;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PaymentService paymentService;

    public Payment createPayment(
            String slug,
            String currency,
            String method,
            String intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Movie movie = movieRepository.findMovieBySlug(slug)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Movie with slug" + slug + " not found!"));

        Amount amount = new Amount();
        amount.setCurrency(currency);
        Double total = new BigDecimal(movie.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(movie.getId().toString());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactionCopied = new ArrayList<>(transactions);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public String executePayment(String paymentId, String payerId, CustomUserDetails currentUser) throws PayPalRESTException {
            StringBuilder content = new StringBuilder();

            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setTransactions(transactionCopied);

            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerId);

            Payment paymentResponse = payment.execute(apiContext, paymentExecute);
            paymentResponse.getPayer().getPayerInfo().setEmail(currentUser.getEmail());
            paymentResponse.setTransactions(payment.getTransactions());

            if (paymentResponse.getState().equals("approved")) {
                String response = paymentService.addPayment(paymentResponse);
                content.append(response);
            }
            return content.toString();
    }

}
