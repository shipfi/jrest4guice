package org.jrest4guice.sample.struts2.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.sample.struts2.model.Employee;
import org.jrest4guice.sample.struts2.service.DepartmentService;
import org.jrest4guice.sample.struts2.service.EmployeeService;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings({"serial","unused","unchecked"})
public class EmployeeAction extends ActionSupport implements Preparable {

	private Log logger = LogFactory.getLog(this.getClass());

	@Inject
	private EmployeeService empService;

	@Inject
	private DepartmentService deptService;

	private Employee employee;

	private List employees;

	private List departments;

	public void prepare() throws Exception {
		departments = deptService.getAllDepartments();
		if (employee != null && employee.getId() != null) {
			employee = empService.getEmployee(employee.getId());
		}
	}

	public String doSave() {
		if (employee.getId() == null) {
			empService.insertEmployee(employee);
		} else {
			empService.updateEmployee(employee);
		}
		return SUCCESS;
	}

	public String doDelete() {
		empService.deleteEmployee(employee.getId());
		return SUCCESS;
	}

	public String doList() {
		employees = empService.getAllEmployees();
		return SUCCESS;
	}

	public String doInput() {
		return INPUT;
	}

	/**
	 * @return Returns the employee.
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            The employee to set.
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	/**
	 * @return Returns the employees.
	 */
	public List getEmployees() {
		return employees;
	}

	/**
	 * @return Returns the departments.
	 */
	public List getDepartments() {
		return departments;
	}
}
