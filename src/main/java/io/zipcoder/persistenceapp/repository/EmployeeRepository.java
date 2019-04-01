package io.zipcoder.persistenceapp.repository;

import io.zipcoder.persistenceapp.entities.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    List<Employee> findEmployeesByDepartmentNumber(Integer deptId);
    List<Employee> findEmployeesByManagerId(Integer managerId);
}
