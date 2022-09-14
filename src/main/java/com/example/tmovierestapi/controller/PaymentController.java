package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.PaymentModel;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentModelResponse;
import com.example.tmovierestapi.service.IPaymentService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {
    @Autowired
    private IPaymentService iPaymentService;

    @GetMapping()
    public ResponseEntity<PagedResponse<PaymentModelResponse>> getAllPayments(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ){
        return new ResponseEntity<>(iPaymentService.getAllPayments(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }
}
