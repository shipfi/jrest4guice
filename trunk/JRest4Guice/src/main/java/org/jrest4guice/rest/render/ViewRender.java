package org.jrest4guice.rest.render;

import java.io.PrintWriter;

import org.jrest4guice.rest.JRestResult;

public interface ViewRender {
	public String getRenderType();
	public void render(PrintWriter out,String templateUrl,JRestResult result) throws Exception;
}
