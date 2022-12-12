package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.enums.PaypalPaymentIntent;
import com.example.tmovierestapi.enums.PaypalPaymentMethod;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.paypal.PaypalService;
import com.example.tmovierestapi.utils.AppConstants;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PaypalController {
    @Autowired
    private PaypalService paypalService;

    //    @Value("${app.rootURL}")
    private String rootURL = "http://localhost:5000/";

    CustomUserDetails currentUser;

    @Value("${app.frontend.url}")
    private String frontendURL;

//    @PostMapping("/pay")

//    public String payment() throws PayPalRESTException {
//        Payment processPayment = paypalService.createPayment("avenger",
//                AppConstants.PAYPAL_CURRENCY,
//                PaypalPaymentMethod.PAYPAL.name(),
//                PaypalPaymentIntent.SALE.name(),
//                rootURL + AppConstants.URL_PAYPAL_CANCEL,
//                rootURL + AppConstants.URL_PAYPAL_SUCCESS);

//
//            for (Links link : processPayment.getLinks()) {
//                if (link.getRel().equals("approval_url")) {
//                    return link.getHref();
//                }
//            }
//            return "redirect:/";
//    }

    @PostMapping("/pay")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Payment payment(@RequestParam("slug")String slug) {
        try {
            Payment payment = paypalService.createPayment(
                    slug,
                    AppConstants.PAYPAL_CURRENCY,
                    PaypalPaymentMethod.PAYPAL.name(),
                    PaypalPaymentIntent.SALE.name(),
                    rootURL + AppConstants.URL_PAYPAL_CANCEL,
                    rootURL + AppConstants.URL_PAYPAL_SUCCESS);
            currentUser = (CustomUserDetails) AppGetLoggedIn.getLoggedIn().getPrincipal();
            return payment;
        } catch (PayPalRESTException e) {
           e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = AppConstants.URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "Cancel!! Payment failed...";
    }

    @GetMapping(value = AppConstants.URL_PAYPAL_SUCCESS)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RedirectView successPay(@Valid @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        String url = paypalService.executePayment(paymentId, payerId, currentUser);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(frontendURL + url);
        return redirectView;
    }

//    @GetMapping(value = AppConstants.URL_PAYPAL_SUCCESS)
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public String successPay(@Valid @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//        try {
//            Payment payment = paypalService.executePayment(paymentId, payerId);
//            if (payment.getState().equals("approved")) {
//                return "Success";
//            }
//        } catch (PayPalRESTException e) {
//            System.out.println(e.getMessage());
//        }
//        return "redirect:/";
//    }

}
