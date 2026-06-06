package com.medical.clinic.service;

import com.medical.clinic.model.Bill;

import java.util.List;

public interface BillService {
    Bill createBill(Bill bill);
    Bill updateBill(String id, Bill bill);
    void deleteBill(String id);
    Bill getBillById(String id);
    List<Bill> getAllBills();
    List<Bill> getBillsByPatientId(String patientId);
    List<Bill> getBillsByAppointmentId(String appointmentId);
}
