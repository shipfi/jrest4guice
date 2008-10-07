package org.jrest4guice.rest.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.validator.InvalidValue;
import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.PageInfo;
import org.jrest4guice.rest.exception.ValidatorException;
import org.jrest4guice.rest.render.ViewRender;
import org.jrest4guice.rest.render.ViewRenderRegister;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class HtmlResponseWriter implements ResponseWriter {
	@Inject
	protected HttpServletRequest request;
	@Inject
	protected HttpSession session;

	public static final String OPTION_KEY = "_$_options_$_";

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_TEXT_HTML;
	}

	@Override
	public void writeResult(Method method, ByteArrayOutputStream out,
			Object result, Map options) {
		try {
			ServiceResult httpResult = ServiceResult.createHttpResult(result);
			// 获取模板路径
			PageFlow annotation = method.getAnnotation(PageFlow.class);
			if (annotation == null) {
				writeTextPlain(out, httpResult);
			} else {
				PageInfo pageInfo = null;
				if (result instanceof Exception) {
					pageInfo = annotation.error();
					if (result instanceof ValidatorException) {
						httpResult
								.setInvalidValues(((ValidatorException) result)
										.getInvalidValues());
						session.setAttribute(ServiceResult.INVALID_VALUE_KEY,
								httpResult.getInvalidValues());
					}

				} else {
					pageInfo = annotation.success();
					Object invalidValues = session
							.getAttribute(ServiceResult.INVALID_VALUE_KEY);
					if (invalidValues != null) {
						httpResult
								.setInvalidValues((InvalidValue[]) invalidValues);
						httpResult.setInChain(true);
					}
					session.removeAttribute(ServiceResult.INVALID_VALUE_KEY);
				}

				if (options != null && options.size() > 0) {
					session
							.setAttribute(HtmlResponseWriter.OPTION_KEY,
									options);
				}

				ViewRender viewRender = ViewRenderRegister.getInstance()
						.getViewRender(pageInfo);

				// 如果模板文件存在，则调用相应的渲染器进行结果的渲染
				if (viewRender != null) {
					viewRender.render(out, annotation, httpResult);
				} else {
					writeTextPlain(out, httpResult);
				}
			}
		} catch (Exception e) {
			System.out.println("向客户端写回数据错误:\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void writeTextPlain(ByteArrayOutputStream out,
			ServiceResult httpResult) {
		try {
			out.write(httpResult.toTextPlain().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
