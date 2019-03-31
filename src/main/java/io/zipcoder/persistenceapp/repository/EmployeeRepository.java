package io.zipcoder.persistenceapp.repository;

import io.zipcoder.persistenceapp.Entities.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    List<Employee> findEmployeesByDepartmentNumber(Integer deptId);
    List<Employee> findEmployeesByManagerId(Integer managerId);
}
