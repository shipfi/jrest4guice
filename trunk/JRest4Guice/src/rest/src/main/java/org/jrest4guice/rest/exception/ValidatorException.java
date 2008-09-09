package org.jrest4guice.rest.exception;

import org.hibernate.validator.InvalidValue;

public class ValidatorException extends RuntimeException {

	private static final long serialVersionUID = -30719057316720963L;
	
	private InvalidValue[] invalidValues;
	
	public ValidatorException(){
		this(null);
	}
	public ValidatorException(InvalidValue[] invalidValues){
		super("validate error");
		this.invalidValues = invalidValues;
	}

	public InvalidValue[] getInvalidValues() {
		return invalidValues;
	}

	public void setInvalidValues(InvalidValue[] invalidValues) {
		this.invalidValues = invalidValues;
	}
}
