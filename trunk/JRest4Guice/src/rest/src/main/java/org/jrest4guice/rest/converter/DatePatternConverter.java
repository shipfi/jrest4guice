package org.jrest4guice.rest.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.Converter;
import org.jrest4guice.rest.helper.JRest4GuiceHelper;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class DatePatternConverter {
	Map<Pattern, String> patterns;

	public DatePatternConverter() {
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

	/**
	 * 为BeanUtilsBean提供缺省的日期转换器
	 */
	public void addDefaultDateConverter() {
		JRest4GuiceHelper.addBeanConvert(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (type != java.sql.Time.class || value == null
						|| value.toString().trim().equals(""))
					return null;
				Object result = DatePatternConverter.this.format(value
						.toString());
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
				Object result = DatePatternConverter.this.format(value
						.toString());
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
				Object result = DatePatternConverter.this.format(value
						.toString());
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

	/**
	 * 注册新的日期转换器
	 * 
	 * @param pattern
	 *            正则表达式
	 * @param patterStr
	 *            日期格式描述的字符串（如:2008-08-08）
	 * @return
	 */
	public DatePatternConverter registDatePattern(Pattern pattern,
			String patterStr) {
		this.patterns.put(pattern, patterStr);
		return this;
	}

	/**
	 * 将字符串格式成日期型数据
	 * 
	 * @param dateStr
	 * @return
	 */
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

	/**
	 * 根据日期字符串返回指定的日期格式，用于SimpleDateFormat的格式描述
	 * 
	 * @param dateStr
	 * @return
	 */
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
