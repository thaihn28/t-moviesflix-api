//package com.example.tmovierestapi.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//@RestControllerAdvice
//public class APIExceptionHandler extends Exception {
//    @Override
//    public Throwable fillInStackTrace() {
//        return this;
//    }
//
//    /**
//     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
//     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public APIException handleAllException(Exception ex, WebRequest request) {
//        // quá trình kiểm soat lỗi diễn ra ở đây
//        return new APIException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
//    }
//
//
//
//    /**
//     * IndexOutOfBoundsException sẽ được xử lý riêng tại đây
//     */
//    @ExceptionHandler(APIException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public APIException TodoException(Exception ex, WebRequest request) {
//        return new APIException(HttpStatus.BAD_REQUEST,  ex.getLocalizedMessage());
//    }
//}
