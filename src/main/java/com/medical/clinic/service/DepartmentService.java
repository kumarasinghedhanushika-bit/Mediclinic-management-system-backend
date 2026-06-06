package com.medical.clinic.service;

import com.medical.clinic.model.Department;
import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department updateDepartment(String id, Department department);
    void deleteDepartment(String id);
    Department getDepartmentById(String id);
    List<Department> getAllDepartments();
}
