package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.enums.PaymentStatus;
import com.medical.clinic.model.Bill;
import com.medical.clinic.repository.BillRepository;
import com.medical.clinic.service.BillService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public Bill createBill(Bill bill) {
        if (bill.getPaymentStatus() == null) {
            bill.setPaymentStatus(PaymentStatus.PENDING);
        }
        if (bill.getCurrency() == null) {
            bill.setCurrency("LKR");
        }
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(String id, Bill bill) {
        Bill existing = getBillById(id);
        existing.setAmount(bill.getAmount());
        existing.setDescription(bill.getDescription());
        existing.setItems(bill.getItems());
        if (bill.getPaymentStatus() != null) {
            existing.setPaymentStatus(bill.getPaymentStatus());
        }
        return billRepository.save(existing);
    }

    @Override
    public void deleteBill(String id) {
        billRepository.deleteById(id);
    }

    @Override
    public Bill getBillById(String id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> getBillsByPatientId(String patientId) {
        return billRepository.findByPatientId(patientId);
    }

    @Override
    public List<Bill> getBillsByAppointmentId(String appointmentId) {
        return billRepository.findByAppointmentId(appointmentId)
                .map(List::of)
                .orElse(List.of());
    }
}
