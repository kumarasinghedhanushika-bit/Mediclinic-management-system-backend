package com.medical.clinic.repository;

import com.medical.clinic.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BillRepository extends MongoRepository<Bill, String> {
    List<Bill> findByPatientId(String patientId);
    java.util.Optional<Bill> findByOrderId(String orderId);
    java.util.Optional<Bill> findByAppointmentId(String appointmentId);
}
