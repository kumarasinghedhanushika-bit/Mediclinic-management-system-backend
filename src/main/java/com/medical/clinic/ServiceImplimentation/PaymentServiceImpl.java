package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.config.PayHereProperties;
import com.medical.clinic.dto.payment.PayHereCheckoutRequest;
import com.medical.clinic.dto.payment.PayHereCheckoutResponse;
import com.medical.clinic.enums.PaymentStatus;
import com.medical.clinic.model.Appointment;
import com.medical.clinic.model.Bill;
import com.medical.clinic.model.User;
import com.medical.clinic.repository.AppointmentRepository;
import com.medical.clinic.repository.BillRepository;
import com.medical.clinic.repository.UserRepository;
import com.medical.clinic.service.PaymentService;
import com.medical.clinic.util.PayHereHashUtil;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PayHereProperties payHereProperties;
    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(
            PayHereProperties payHereProperties,
            BillRepository billRepository,
            AppointmentRepository appointmentRepository,
            UserRepository userRepository
    ) {
        this.payHereProperties = payHereProperties;
        this.billRepository = billRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PayHereCheckoutResponse initiateCheckout(PayHereCheckoutRequest request, String patientEmail) {
        User user = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getPatientUserId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to pay for this appointment");
        }

        billRepository.findByAppointmentId(appointment.getId()).ifPresent(b -> {
            if (b.getPaymentStatus() == PaymentStatus.PAID) {
                throw new RuntimeException("Appointment already paid");
            }
        });

        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        double amount = request.getAmount() != null
                ? request.getAmount()
                : (appointment.getConsultationFee() != null ? appointment.getConsultationFee() : 0);

        if (amount <= 0) {
            throw new RuntimeException("Invalid payment amount");
        }

        String formattedAmount = new DecimalFormat("0.00").format(amount);
        String hash = PayHereHashUtil.generateCheckoutHash(
                payHereProperties.getMerchantId(),
                orderId,
                amount,
                payHereProperties.getCurrency(),
                payHereProperties.getMerchantSecret()
        );

        Bill bill = Bill.builder()
                .orderId(orderId)
                .patientId(appointment.getPatientId())
                .appointmentId(appointment.getId())
                .amount(amount)
                .currency(payHereProperties.getCurrency())
                .description("Consultation fee - " + appointment.getAppointmentNumber())
                .items(request.getItems() != null ? request.getItems() : "Doctor channeling fee")
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        bill = billRepository.save(bill);

        PayHereCheckoutResponse response = new PayHereCheckoutResponse();
        response.setSandbox(payHereProperties.isSandbox());
        response.setCheckoutUrl(payHereProperties.getCheckoutUrl());
        response.setMerchantId(payHereProperties.getMerchantId());
        response.setOrderId(orderId);
        response.setAmount(formattedAmount);
        response.setCurrency(payHereProperties.getCurrency());
        response.setHash(hash);
        response.setReturnUrl(payHereProperties.getReturnUrl());
        response.setCancelUrl(payHereProperties.getCancelUrl());
        response.setNotifyUrl(payHereProperties.getNotifyUrl());
        response.setItems(bill.getItems());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone() != null ? user.getPhone() : "0770000000");
        response.setAddress("Clinic");
        response.setCity("Colombo");
        response.setCountry("Sri Lanka");
        response.setBillId(bill.getId());
        return response;
    }

    @Override
    public void handlePayHereNotify(Map<String, String> params) {
        String merchantId = params.get("merchant_id");
        String orderId = params.get("order_id");
        String payhereAmount = params.get("payhere_amount");
        String payhereCurrency = params.get("payhere_currency");
        String statusCode = params.get("status_code");
        String md5sig = params.get("md5sig");
        String paymentId = params.get("payment_id");

        if (merchantId == null || orderId == null || payhereAmount == null || statusCode == null || md5sig == null) {
            throw new RuntimeException("Invalid PayHere notification");
        }

        double amount = Double.parseDouble(payhereAmount);
        String currency = payhereCurrency != null ? payhereCurrency : payHereProperties.getCurrency();

        String expectedSig = PayHereHashUtil.generateNotifySignature(
                merchantId,
                orderId,
                amount,
                currency,
                statusCode,
                payHereProperties.getMerchantSecret()
        );

        if (!expectedSig.equalsIgnoreCase(md5sig)) {
            throw new RuntimeException("Invalid PayHere signature");
        }

        Bill bill = billRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Bill not found for order"));

        bill.setPayherePaymentId(paymentId);
        bill.setPayhereStatusCode(statusCode);
        bill.setPayhereStatusMessage(params.get("status_message"));

        if ("2".equals(statusCode)) {
            bill.setPaymentStatus(PaymentStatus.PAID);
            bill.setPaidAt(LocalDateTime.now());
        } else {
            bill.setPaymentStatus(PaymentStatus.FAILED);
        }

        billRepository.save(bill);
    }

    @Override
    public Bill getBillByOrderId(String orderId) {
        return billRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }
}
