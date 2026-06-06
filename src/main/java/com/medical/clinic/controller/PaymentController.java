package com.medical.clinic.controller;

import com.medical.clinic.dto.payment.PayHereCheckoutRequest;
import com.medical.clinic.dto.payment.PayHereCheckoutResponse;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Bill;
import com.medical.clinic.security.SecurityUtils;
import com.medical.clinic.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
@Tag(name = "Payments", description = "PayHere payment gateway")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payhere/checkout")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Initiate PayHere checkout for appointment payment")
    public ResponseEntity<ApiResponse<PayHereCheckoutResponse>> checkout(
            @Valid @RequestBody PayHereCheckoutRequest request
    ) {
        PayHereCheckoutResponse response = paymentService.initiateCheckout(
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Checkout prepared", false, true, response));
    }

    @PostMapping("/payhere/notify")
    @Operation(summary = "PayHere server notify URL (no auth)")
    public ResponseEntity<String> notify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            params.put(name, request.getParameter(name));
        }
        paymentService.handlePayHereNotify(params);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/status/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Bill>> status(@PathVariable String orderId) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Payment status",
                false,
                true,
                paymentService.getBillByOrderId(orderId)
        ));
    }
}
