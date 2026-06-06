package com.medical.clinic.service;

import com.medical.clinic.dto.payment.PayHereCheckoutRequest;
import com.medical.clinic.dto.payment.PayHereCheckoutResponse;
import com.medical.clinic.model.Bill;

import java.util.Map;

public interface PaymentService {
    PayHereCheckoutResponse initiateCheckout(PayHereCheckoutRequest request, String patientEmail);
    void handlePayHereNotify(Map<String, String> params);
    Bill getBillByOrderId(String orderId);
}
