package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.PaymentModel;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentModelResponse;
import com.example.tmovierestapi.service.IPaymentService;
import com.example.tmovierestapi.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/payments")
@Tag(name = "payment")
public class PaymentController {
    @Autowired
    private IPaymentService iPaymentService;

    @Operation(description = "View all list payments", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = PaymentModel.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

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
