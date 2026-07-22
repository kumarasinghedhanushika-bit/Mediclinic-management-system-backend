package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.enums.PaymentStatus;
import com.medical.clinic.model.Appointment;
import com.medical.clinic.model.Bill;
import com.medical.clinic.model.Patient;
import com.medical.clinic.repository.AppointmentRepository;
import com.medical.clinic.repository.BillRepository;
import com.medical.clinic.repository.PatientRepository;
import com.medical.clinic.service.BillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public BillServiceImpl(
            BillRepository billRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }


    @Override
    public Bill createBill(Bill bill) {
        if (bill.getPaymentStatus() == null) {
            bill.setPaymentStatus(PaymentStatus.PENDING);
        }
        if (bill.getCurrency() == null) {
            bill.setCurrency("LKR");
        }
        if (bill.getBillNumber() == null) {
            bill.setBillNumber("BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (bill.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findByAppointmentNumber(bill.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));

            // Pull these from the appointment so the client can never spoof them
            bill.setConsultationFee(appointment.getConsultationFee());
            bill.setAppointmentNumber(appointment.getAppointmentNumber());
            bill.setPatientName(appointment.getPatientName());

            if (bill.getPatientId() == null) {
                bill.setPatientId(appointment.getPatientId());
            }

            Patient patient = patientRepository.findById(appointment.getPatientId()).orElse(null);
            if (patient != null) {
                bill.setPatientNumber(patient.getPatientNumber());
            }
        }

        if (bill.getHospitalCharge() == null) {
            bill.setHospitalCharge(0.0);
        }

        recalculateAmount(bill);

        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(String id, Bill bill) {
        Bill existing = getBillById(id);

        existing.setDescription(bill.getDescription());
        existing.setItems(bill.getItems());

        if (bill.getConsultationFee() != null) {
            existing.setConsultationFee(bill.getConsultationFee());
        }
        if (bill.getHospitalCharge() != null) {
            existing.setHospitalCharge(bill.getHospitalCharge());
        }
        if (bill.getPaymentStatus() != null) {
            existing.setPaymentStatus(bill.getPaymentStatus());
        }

        recalculateAmount(existing);

        return billRepository.save(existing);
    }

    @Override
    public void deleteBill(String id) {
        billRepository.deleteById(id);
    }

    @Override
    public Bill getBillById(String id) {
        System.out.println("billing id " + id);
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> getBillsByPatientId(String patientId) {

        System.out.println("billing patient id: " + patientId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        System.out.println("Patient found: " +
                patient.getId() + " / " + patient.getPatientNumber());

        return billRepository.findByPatientId(patient.getId());
    }

    @Override
    public List<Bill> getBillsByAppointmentId(String appointmentId) {
        return billRepository.findByAppointmentId(appointmentId)
                .map(List::of)
                .orElse(List.of());
    }

    private void recalculateAmount(Bill bill) {
        double consultationFee = bill.getConsultationFee() != null ? bill.getConsultationFee() : 0.0;
        double hospitalCharge = bill.getHospitalCharge() != null ? bill.getHospitalCharge() : 0.0;
        bill.setAmount(consultationFee + hospitalCharge);
    }
}