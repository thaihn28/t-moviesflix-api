package com.example.tmovierestapi.service.paypal;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.repository.MovieRepository;
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

    public String createPayment(
            Movie movie,
            String currency,
            String method,
            String intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        try {
            Amount amount = new Amount();
            amount.setCurrency(currency);
//        amount.setTotal(movie.getPrice().toString());
            amount.setTotal(String.format("%.2f", movie.getPrice()));
//        Double price = new BigDecimal(movie.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setPurchaseUnitReferenceId(movie.getId().toString());
//        List<Transaction> transactions = new ArrayList<>();
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

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        try {
            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setTransactions(transactions);
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerId);
            Payment paymentResponse = payment.execute(apiContext, paymentExecute);
            paymentResponse.getTransactions().stream().filter(item -> item.getPurchaseUnitReferenceId() != null)
                    .collect(Collectors.toList());
            paymentResponse.setTransactions(payment.getTransactions());
            if (paymentResponse.getState().equals("approved")) {

                for (Transaction transaction : paymentResponse.getTransactions()) {
                    Long movieID = Long.valueOf(transaction.getPurchaseUnitReferenceId());
                    Movie movie = movieRepository.findMovieById(movieID)
                            .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Can not found Movie ID-" + movieID));
                    movie.setIsFree(true);
//                    movie.setPrice(0d);
                    movieRepository.save(movie);

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
                    return paymentResponse;
                }
            }
        } catch (PayPalRESTException e) {
            throw new PayPalRESTException(e.getMessage());
        }
        return null;
    }

}
