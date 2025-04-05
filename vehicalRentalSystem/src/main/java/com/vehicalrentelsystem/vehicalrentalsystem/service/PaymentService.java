//package com.vehicalrentelsystem.vehicalrentalsystem.service;
//
//import com.vehicalrentelsystem.vehicalrentalsystem.dto.PaymentDTO;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.Payment;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.PaymentStatus;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.PaymentRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentService {
//
//    private final PaymentRepository paymentRepository;
//
//    public PaymentService(PaymentRepository paymentRepository) {
//        this.paymentRepository = paymentRepository;
//    }
//
//    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
//        Payment payment = new Payment();
//        payment.setBookingId(paymentDTO.getBookingId());
//        payment.setAmount(paymentDTO.getAmount());
//        payment.setStatus(PaymentStatus.SUCCESS); // Assuming payment succeeds
//        paymentRepository.save(payment);
//
//        paymentDTO.setId(payment.getId());
//        paymentDTO.setStatus(payment.getStatus().name());
//        return paymentDTO;
//    }
//
//    public PaymentDTO getPaymentStatus(Long paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//
//        return new PaymentDTO(
//                payment.getId(),
//                payment.getBookingId(),
//                payment.getAmount(),
//                payment.getStatus().name()
//        );
//    }
//}
