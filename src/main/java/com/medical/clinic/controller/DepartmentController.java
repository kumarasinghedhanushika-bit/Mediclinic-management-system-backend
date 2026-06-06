package com.medical.clinic.controller;

import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Department;
import com.medical.clinic.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin("*")
@Tag(name = "Departments", description = "Hospital departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Department>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>("Departments fetched", false, true, departmentService.getAllDepartments()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Department>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Department fetched", false, true, departmentService.getDepartmentById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Department>> create(@Valid @RequestBody Department department) {
        return ResponseEntity.ok(new ApiResponse<>("Department created", false, true, departmentService.createDepartment(department)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Department>> update(@PathVariable String id, @RequestBody Department department) {
        return ResponseEntity.ok(new ApiResponse<>("Department updated", false, true, departmentService.updateDepartment(id, department)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(new ApiResponse<>("Department deleted", false, true, null));
    }
}
