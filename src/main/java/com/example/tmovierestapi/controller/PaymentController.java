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
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    @Autowired
    private PaypalService paypalService;

    @Value("${app.rootURL}")
    private String rootURL;

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/pay")
    public String payment(@RequestParam(name = "movieID") Long movieID) {
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

            for(Links link : payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
//                    return "redirect:"+link.getHref();
//                    return response.sendRedirect(link.getHref());
//                    return new RedirectView(link.getHref());
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "/";
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
//            if (payment.getState().equals("approved")) {
//            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

}
