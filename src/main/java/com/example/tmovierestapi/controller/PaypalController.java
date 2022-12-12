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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PaypalController {
    @Autowired
    private PaypalService paypalService;

    @Value("${app.rootURL}")
    private String rootURL;

    CustomUserDetails currentUser;

    @Value("${app.frontend.url}")
    private String frontendURL;


    @PostMapping("/pay")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Payment payment(@RequestParam("slug") String slug) throws PayPalRESTException {
        Payment payment = paypalService.createPayment(
                slug,
                AppConstants.PAYPAL_CURRENCY,
                PaypalPaymentMethod.PAYPAL.name(),
                PaypalPaymentIntent.ORDER.name(),
                rootURL + AppConstants.URL_PAYPAL_CANCEL,
                rootURL + AppConstants.URL_PAYPAL_SUCCESS);
        currentUser = (CustomUserDetails) AppGetLoggedIn.getLoggedIn().getPrincipal();
        return payment;
    }

    @GetMapping(value = AppConstants.URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "Cancel!! Payment failed...";
    }

    @GetMapping(value = AppConstants.URL_PAYPAL_SUCCESS)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RedirectView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        String url = paypalService.executePayment(paymentId, payerId, currentUser);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(frontendURL + url);
        return redirectView;
    }

}
