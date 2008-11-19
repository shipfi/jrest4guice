package org.jrest4guice.persistence;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.ObjectUtils.Null;
import org.jrest4guice.commons.lang.Logic;
import org.jrest4guice.commons.lang.Relation;

public class ParameterObject {
	private String name;
	private Object value;
	private Logic logic;
	private Relation relation;
	private Class<?> dataType;
	
	public ParameterObject(String name, Object value, Logic logic, Relation relation) {
		this(name,value,logic,relation,Null.class);
	}
	public ParameterObject(String name, Object value, Logic logic, Relation relation,Class<?> dataType) {
		super();
		this.name = name;
		this.value = value;
		this.logic = logic;
		this.relation = relation;
		this.dataType = dataType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		if(this.dataType ==  Null.class)
			return value;
		else{
			Object convert = BeanUtilsBean.getInstance().getConvertUtils().convert(value, this.dataType);
//			System.out.println(((Date)convert).toLocaleString());
			return convert;
		}
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Logic getLogic() {
		return logic;
	}
	public void setLogic(Logic logic) {
		this.logic = logic;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public String getLogicSymbol() {
		String symbol = "and";
		switch (this.logic) {
		case AND:
			symbol = "and";
			break;
		case OR:
			symbol = "or";
			break;
		}
		return symbol;
	}

	public String getRelationSymbol() {
		String symbol = "=";
		switch (this.relation) {
		case EQUAL:
			symbol = "=";
			break;
		case NOT_EQUAL:
			symbol = "!=";
			break;
		case GREAT_THAN:
			symbol = ">";
			break;
		case GREAT_EQUAL_THAN:
			symbol = ">=";
			break;
		case LESS_THEN:
			symbol = "<";
			break;
		case LESS_EQUAL_THEN:
			symbol = "<=";
			break;
		}
		return symbol;
	}
}
