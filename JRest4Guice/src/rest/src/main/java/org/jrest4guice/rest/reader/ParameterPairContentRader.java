package org.jrest4guice.rest.reader;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.rest.annotations.RESTful;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings( { "unchecked" })
public abstract class ParameterPairContentRader implements RequestContentReader {
	@Override
	public void readData(HttpServletRequest request, ModelMap params,
			String charset) {
		Enumeration names = request.getAttributeNames();
		String name;
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			params.put(name, request.getAttribute(name));
		}

		// url中的参数
		names = request.getParameterNames();
		String value;
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			try {
				//TODO url中的参数乱码
				if (RESTful.METHOD_OF_GET.equalsIgnoreCase(request.getMethod()))
					value = new String(request.getParameter(name).getBytes("ISO-8859-1"), charset);
				else
					value = request.getParameter(name);
				params.put(name, value);
//				if(name.equalsIgnoreCase("search")){
//					System.out.println(new String(request.getParameter(name).getBytes("ISO-8859-1"), charset));
//				}
			} catch (Exception e) {
				params.put(name, request.getParameter(name));
			}
		}
	}
}
