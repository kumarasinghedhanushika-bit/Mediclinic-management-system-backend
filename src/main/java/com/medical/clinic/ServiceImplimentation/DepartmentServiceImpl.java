package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.model.Department;
import com.medical.clinic.repository.DepartmentRepository;
import com.medical.clinic.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(String id, Department department) {
        Department existing = getDepartmentById(id);
        existing.setName(department.getName());
        existing.setDescription(department.getDescription());
        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(String id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public Department getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
