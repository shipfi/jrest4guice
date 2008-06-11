package org.jrest4guice.rest.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.Converter;
import org.jrest4guice.rest.JRest4GuiceHelper;

public class DateConverter {
	Map<Pattern, String> patterns;

	public DateConverter() {
		this.patterns = new HashMap<Pattern, String>();
		this.patterns.put(Pattern
				.compile("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})"), "yyyy-MM-dd");

		this.patterns
				.put(
						Pattern
								.compile("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})"),
						"yyyy-MM-dd HH:mm:ss");

		this.patterns.put(Pattern
				.compile("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"), "MM/dd/yyyy");
		this.patterns
				.put(
						Pattern
								.compile("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})"),
						"MM/dd/yyyy HH:mm:ss");

	}

	public void addDefaultDateConverter() {
		JRest4GuiceHelper.addBeanConvert(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (type != java.sql.Time.class || value == null
						|| value.toString().trim().equals(""))
					return null;
				Object result = DateConverter.this.format(value.toString());
				if (result == null) {
					try {
						result = new java.sql.Time(Long.parseLong(value
								.toString()));
					} catch (Exception e) {
					}
				}
				return result;
			}
		}, java.sql.Time.class);

		JRest4GuiceHelper.addBeanConvert(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (type != java.util.Date.class || value == null
						|| value.toString().trim().equals(""))
					return null;
				Object result = DateConverter.this.format(value.toString());
				if (result == null) {
					try {
						result = new java.util.Date(Long.parseLong(value
								.toString()));
					} catch (Exception e) {
					}
				}
				return result;
			}
		}, java.util.Date.class);

		JRest4GuiceHelper.addBeanConvert(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (type != java.sql.Date.class || value == null
						|| value.toString().trim().equals(""))
					return null;
				Object result = DateConverter.this.format(value.toString());
				if (result == null) {
					try {
						result = new java.sql.Date(Long.parseLong(value
								.toString()));
					} catch (Exception e) {
					}
				}
				return result;
			}
		}, java.sql.Date.class);
	}

	public DateConverter registDatePattern(Pattern pattern, String patterStr) {
		this.patterns.put(pattern, patterStr);
		return this;
	}

	public Date format(String dateStr) {
		String patterStr = this.getPatternString(dateStr);
		SimpleDateFormat sdf = null;
		if (patterStr != null)
			sdf = new SimpleDateFormat(patterStr);
		else
			sdf = new SimpleDateFormat();

		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getPatternString(String dateStr) {
		String pattern = null;
		for (Pattern p : this.patterns.keySet()) {
			if (p.matcher(dateStr).matches()) {
				pattern = this.patterns.get(p);
				break;
			}
		}
		return pattern;
	}
}
