package org.jrest4guice.sample.struts2.service;

import java.util.List;

import org.jrest4guice.persistence.BaseEntityManager;
import org.jrest4guice.sample.struts2.model.Employee;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

public class EmployeeService{
	@Inject
	private DepartmentService departmentService;

	@Inject
	private BaseEntityManager<String, Employee> entityManager;
	

	@Transactional(type=TransactionalType.READOLNY)
    public List<Employee> getAllEmployees() {
        return entityManager.listAll();
    }

	@Transactional
    public void updateEmployee(Employee emp) {
        entityManager.update(emp);
    }

	@Transactional
    public void deleteEmployee(String id) {
        entityManager.deleteById(id);
    }

	@Transactional(type=TransactionalType.READOLNY)
    public Employee getEmployee(String id) {
        return entityManager.load(id);
    }

	@Transactional
    public void insertEmployee(Employee emp) {
		System.out.println(this.departmentService.getDepartment(emp.getDepartment().getId()).getName());
        entityManager.create(emp);
    }
}
