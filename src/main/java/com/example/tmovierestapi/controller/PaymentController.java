package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.enums.PaypalPaymentIntent;
import com.example.tmovierestapi.enums.PaypalPaymentMethod;
import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.request.PurchaseRequest;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.paypal.PaypalService;
import com.example.tmovierestapi.utils.AppConstants;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PaymentController {

    @Autowired
    private PaypalService paypalService;

    @Value("${app.rootURL}")
    private String rootURL;

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/pay")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<String> payment(@RequestParam(name = "movie-id") Long movieID) throws PayPalRESTException {
//        Payment payment = null;
        Movie movie = movieRepository.findMovieById(movieID)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST , "Movie with ID" + movieID + " not found!"));
        try {
//            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
//                    order.getIntent(), order.getDescription(), "http://localhost:9090/" + CANCEL_URL,
//                    "http://localhost:9090/" + SUCCESS_URL);

            Payment payment = paypalService.createPayment(movie,
                    AppConstants.PAYPAL_CURRENCY,
                    PaypalPaymentMethod.PAYPAL.name(),
                    PaypalPaymentIntent.SALE.name(),
                    rootURL + AppConstants.URL_PAYPAL_CANCEL,
                    rootURL + AppConstants.URL_PAYPAL_SUCCESS);

            System.out.println(payment.toJSON());

            for(Links link : payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return new ResponseEntity<>(link.getHref(), HttpStatus.OK);
                }
            }
        } catch (PayPalRESTException e) {
            throw new PayPalRESTException(e.getMessage());
        }
        return null;
//        return new ResponseEntity<>(purchaseRequest ,HttpStatus.CREATED);
    }
    @GetMapping(value = AppConstants.URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "Cancel payment";
    }

    @GetMapping(value = AppConstants.URL_PAYPAL_SUCCESS)
    public ResponseEntity<Payment> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        Payment paymentResponse = null;
        try {
//            Payment payment = paypalService.executePayment(paymentId, payerId);
             paymentResponse = paypalService.executePayment(paymentId, payerId);
            if (paymentResponse.getState().equals("approved")) {
                for(Transaction transaction : paymentResponse.getTransactions()){
                    Long movieID = Long.valueOf(transaction.getPurchaseUnitReferenceId());
                    Movie movie = movieRepository.findMovieById(movieID)
                            .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Can not found Movie ID-"+ movieID));
                    movie.setIsFree(true);
                    movie.setPrice(null);
                    movieRepository.save(movie);

                    /*TODO: Add to Purchase table
                    * paymentResponse.getPayer().getStatus() -> VERIFIED
                    * paymentResponse.getPayer().getPaymentMethod() -> paypal
                    * paymentResponse.getState() -> Approved
                    * * paymentResponse.getPayer().getPayerInfo().getEmail() -> Email
                    * paymentResponse.getPayer().getPayerInfo().getShippingAddress() -> Name, Address
                    * transaction.getAmount.getTotal -> Price -> Transaction
                    * transaction.getCurrency.getTotal -> Price -> Transaction
                    */
                }
                return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
