package org.jrest4guice.sample.struts2.service;

import java.util.List;

import org.jrest4guice.persistence.BaseEntityManager;
import org.jrest4guice.sample.struts2.model.Department;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

public class DepartmentService{
	@Inject
	private BaseEntityManager<String, Department> entityManager;

	@Transactional(type=TransactionalType.READOLNY)
    public List<Department> getAllDepartments() {
        return entityManager.listAll();
    }

	@Transactional(type=TransactionalType.READOLNY)
	public Department getDepartment(String id) {
		return this.entityManager.load(id);
	}
}
