package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.entities.Department;
import io.zipcoder.persistenceapp.entities.Employee;
import io.zipcoder.persistenceapp.repository.DepartmentRepository;
import io.zipcoder.persistenceapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeDirectoryController {

    //@Autowired
    private DepartmentRepository departmentRepository;
    //@Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDirectoryController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/Employee/")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee e) {
        return new ResponseEntity<>(employeeRepository.save(e), HttpStatus.CREATED);
    }

    @PostMapping("/Department")
    public ResponseEntity<Department> createDepartment(@RequestBody Department d) {
        return new ResponseEntity<>(departmentRepository.save(d), HttpStatus.CREATED);
    }

    @PutMapping("/Employee/{employeeId}/mgr/{managerId}")
    public ResponseEntity<Employee> updateEmployeeMgr(@PathVariable int employeeId, @PathVariable int managerId) {

        Employee employee = employeeRepository.findOne(employeeId);
        employee.setManagerId(managerId);

        return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.OK);
    }

    @PutMapping("/Employee/")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee e) {
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
    public ResponseEntity<Department> updateDepartmentMgr(@PathVariable int deptId, @PathVariable int mgrId) {
        Department department = departmentRepository.findOne(deptId);
        department.setManagerId(mgrId);

        return new ResponseEntity<>(departmentRepository.save(department), HttpStatus.OK);

    }

    @PutMapping("Department")
    public ResponseEntity<Department> updateDepartment(@RequestBody Department d) {
        Department department = departmentRepository.findOne(d.getDepartmentId());
        department.setName(d.getName());

        return new ResponseEntity<>(departmentRepository.save(department), HttpStatus.OK);
    }

    @GetMapping("/Employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        return new ResponseEntity<>(employeeRepository.findOne(id), HttpStatus.OK);
    }

    @GetMapping("/Employees/{mgrId}")
    public ResponseEntity<List<Employee>> getEmployeesByMgr(@PathVariable int mgrId) {
        return new ResponseEntity<>(employeeRepository.findEmployeesByManagerId(mgrId), HttpStatus.OK);
    }


    @GetMapping("/Hierarchy/{id}")
    public ResponseEntity<List<Employee>> getHierarchy(@PathVariable int id) {
        List<Employee> employees = employeeRepository.findEmployeesByManagerId(id);
        if (employees != null) {
            employees.addAll(getSuborinates(employees));
        }

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    private List<Employee> getSuborinates(List<Employee> employees) {
        List<Employee> retVal = new ArrayList<>();

        for (Employee ele : employees) {
            retVal.addAll(employeeRepository.findEmployeesByManagerId(ele.getManagerId()));
        }

        return retVal;
    }

    @GetMapping("/ReverseHierarchy/{id}")
    public ResponseEntity<List<Employee>> getReverseHierarchy(@PathVariable int id) {

        Employee employee = employeeRepository.findOne(id);
        List<Employee> retVal = new ArrayList<>();
        if (employee != null) {
            retVal.add(employee);
            while (employee.getManagerId() != null) {
                employee = employeeRepository.findOne(employee.getId());
                retVal.add(employee);
            }
        }

        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @GetMapping("/Employees/")
    public ResponseEntity<List<Employee>> getEmployeesWithoutManager() {

        return new ResponseEntity<>(employeeRepository.findEmployeesByManagerId(null), HttpStatus.OK);
    }

    @GetMapping("/Employees/dept/{deptId}")
    public ResponseEntity<List<Employee>> getEmployeesByDept(@PathVariable int deptId) {

        return new ResponseEntity<>(employeeRepository.findEmployeesByDepartmentNumber(deptId), HttpStatus.OK);
    }

    @DeleteMapping("/Employee/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Integer id) {
        employeeRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/Employees/{mgrId}")
    public ResponseEntity<Employee> deleteEmployeesFromManager(@PathVariable Integer mgrId) {
        List<Employee> employees = employeeRepository.findEmployeesByManagerId(mgrId);
        if (employees != null) {
            employees.addAll(getSuborinates(employees));
        }
        if (employees != null) {
            employees.forEach(employee -> employeeRepository.delete(employee.getId()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/Employees/RemoveMgr/{mgrId}")
    public ResponseEntity<Employee> deleteManager(@PathVariable Integer mgrId) {
        Employee oldManager = employeeRepository.findOne(mgrId);
        List<Employee> employees = employeeRepository.findEmployeesByManagerId(mgrId);
        employees.forEach(employee -> employee.setManagerId(oldManager.getManagerId()));
        employeeRepository.save(employees);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/DepartmentMerge/{deptIdMergedFrom}/to/{deptIdMergedTo}")
    public ResponseEntity<Department> mergeDepartments(@PathVariable Integer deptIdMergedFrom, @PathVariable Integer deptIdMergedTo) {
        List<Employee> employees = employeeRepository.findEmployeesByDepartmentNumber(deptIdMergedFrom);
        employees.forEach(employee -> employee.setDepartmentNumber(deptIdMergedTo));

        Department oldDept = departmentRepository.findOne(deptIdMergedFrom);
        Employee oldManager = employeeRepository.findOne(oldDept.getManagerId());
        Department newDept = departmentRepository.findOne(deptIdMergedTo);
        oldManager.setManagerId(newDept.getManagerId());

        employeeRepository.save(employees);
        employeeRepository.save(oldManager);


        return new ResponseEntity<>(HttpStatus.OK);
    }


}
