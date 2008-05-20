package org.cnoss.core.persist.jpa;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.TemporalType;

public class TemporalValue {

	private Calendar value;
	private TemporalType type;

	public TemporalValue(final Calendar value, final TemporalType type) {
		if (value == null || type == null) {
			throw new IllegalArgumentException("必要参数为空");
		}
		this.value = value;
		this.type = type;
	}

	public TemporalValue(final Date date, final TemporalType type) {
		if (date == null || type == null) {
			throw new IllegalArgumentException("必要参数为空");
		}
		this.value = Calendar.getInstance();
		this.value.setTime(date);
		this.type = type;
	}

	public TemporalType getType() {
		return this.type;
	}

	public Calendar getValue() {
		return this.value;
	}

	public void setType(final TemporalType type) {
		this.type = type;
	}

	public void setValue(final Calendar value) {
		this.value = value;
	}
}
