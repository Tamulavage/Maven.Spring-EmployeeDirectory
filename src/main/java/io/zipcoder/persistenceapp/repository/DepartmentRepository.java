package io.zipcoder.persistenceapp.repository;

import io.zipcoder.persistenceapp.Entities.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
}
