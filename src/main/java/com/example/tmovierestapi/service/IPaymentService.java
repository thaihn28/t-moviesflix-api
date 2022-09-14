package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.PaymentModel;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentModelResponse;
import com.paypal.api.payments.Payment;

public interface IPaymentService {
    PagedResponse<PaymentModelResponse> getAllPayments(int pageNo, int pageSize, String sortDir, String sortBy);
    PaymentModel addPayment(Payment payment);
}
