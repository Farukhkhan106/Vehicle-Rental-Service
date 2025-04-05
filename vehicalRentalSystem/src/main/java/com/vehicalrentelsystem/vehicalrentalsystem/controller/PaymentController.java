//package com.vehicalrentelsystem.vehicalrentalsystem.controller;
//
//import com.vehicalrentelsystem.vehicalrentalsystem.dto.PaymentDTO;
//import com.vehicalrentelsystem.vehicalrentalsystem.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/payment")
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @PostMapping("/process")
//    public ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentDTO) {
//        paymentService.processPayment(paymentDTO);
//        return ResponseEntity.ok("Payment processed successfully!");
//    }
//
//    @GetMapping("/{paymentId}")
//    public ResponseEntity<PaymentDTO> getPaymentStatus(@PathVariable Long paymentId) {
//        PaymentDTO paymentDTO = paymentService.getPaymentStatus(paymentId);
//        return ResponseEntity.ok(paymentDTO);
//    }
//}
