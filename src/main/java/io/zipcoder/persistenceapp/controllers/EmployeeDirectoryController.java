package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.Entities.Department;
import io.zipcoder.persistenceapp.Entities.Employee;
import io.zipcoder.persistenceapp.repository.DepartmentRepository;
import io.zipcoder.persistenceapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeDirectoryController {

    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDirectoryController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository ){
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/Employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee e){
        return new ResponseEntity<>(employeeRepository.save(e), HttpStatus.CREATED);
    }

    @PostMapping("/Department")
    public ResponseEntity<Department> createDepartment(@RequestBody Department d){
        return new ResponseEntity<>(departmentRepository.save(d), HttpStatus.CREATED);
    }

    @PutMapping("/Employee/{employeeId}/mgr/{managerId}")
    public ResponseEntity<Employee> updateEmployeeMgr(@PathVariable int employeeId,@PathVariable int managerId) {

         Employee employee = employeeRepository.findOne(employeeId);
         employee.setManagerId(managerId);

         return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.OK);
    }

    @PutMapping("/Employee")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee e){
        Employee employee = employeeRepository.findOne(e.getId());
        employee.setEmail(e.getEmail());
        employee.setManagerId(e.getManagerId());
        employee.setDepartmentNumber(e.getDepartmentNumber());
        employee.setFname(e.getFname());
        employee.setLname(e.getLname());
        employee.setHireDate(e.getHireDate());
        employee.setPhoneNumber(e.getPhoneNumber());
        employee.setTitle(e.getTitle());

        return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.OK);
    }

    @PutMapping("/Department/{deptId}/mgr/{mgrId}")
    public ResponseEntity<Department> updateDepartmentMgr(@PathVariable int deptId, @PathVariable int mgrId){
        Department department = departmentRepository.findOne(deptId);
        department.setManagerId(mgrId);

        return new ResponseEntity<>(departmentRepository.save(department), HttpStatus.OK);

    }

    @PutMapping("Department")
    public ResponseEntity<Department> updateDepartment(@RequestBody Department d){
        Department department = departmentRepository.findOne(d.getDepartmentId());
        department.setName(d.getName());

        return new ResponseEntity<>(departmentRepository.save(department), HttpStatus.OK);
    }

    @GetMapping("/Employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id){
        return new ResponseEntity<>(employeeRepository.findOne(id), HttpStatus.OK);
    }





}
