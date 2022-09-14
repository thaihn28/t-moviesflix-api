package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.enums.PaypalPaymentIntent;
import com.example.tmovierestapi.enums.PaypalPaymentMethod;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.paypal.PaypalService;
import com.example.tmovierestapi.utils.AppConstants;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PaypalController {

    @Autowired
    private PaypalService paypalService;

    @Value("${app.rootURL}")
    private String rootURL;

    CustomUserDetails currentUser;


    @PostMapping("/pay")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<String> payment(@Valid @RequestParam(name = "movie-id") Long movieID) throws PayPalRESTException {
            String processPaymentLink = paypalService.createPayment(movieID,
                    AppConstants.PAYPAL_CURRENCY,
                    PaypalPaymentMethod.PAYPAL.name(),
                    PaypalPaymentIntent.SALE.name(),
                    rootURL + AppConstants.URL_PAYPAL_CANCEL,
                    rootURL + AppConstants.URL_PAYPAL_SUCCESS);
        currentUser = (CustomUserDetails) AppGetLoggedIn.getLoggedIn().getPrincipal();

        return new ResponseEntity<>(processPaymentLink ,HttpStatus.OK);
    }
    @GetMapping(value = AppConstants.URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "Cancel payment";
    }

    @GetMapping(value = AppConstants.URL_PAYPAL_SUCCESS)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Payment> successPay(@Valid @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        return new ResponseEntity<>(paypalService.executePayment(paymentId, payerId, currentUser), HttpStatus.OK);

    }

}
