package org.jrest4guice.persistence.ibatis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jrest4guice.transaction.IbatisTransaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.inject.Singleton;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Singleton
public class SqlMapClientHolder {
	private static SqlMapClient sqlMapClient;
	private static List<Class<?>> daos = new ArrayList<Class<?>>(0);
	
	private final ThreadLocal<IbatisTransaction> transaction = new ThreadLocal<IbatisTransaction>();

	public static void addIbatisDao(Class<?> clazz) {
		daos.add(clazz);
	}

	public static void initSqlMapClient() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			sb.append("<!DOCTYPE sqlMap      ");
			sb.append("    PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" ");     
			sb.append("\"http://ibatis.apache.org/dtd/sql-map-2.dtd\">");
			sb.append("<sqlMap>");

			for (Class clazz : daos) {
				sb.append(SqlMapClientXmlHelper.generateXmlConfig(clazz));
			}
			sb.append("</sqlMap>");

			File sqlMapConfigFile = Resources.getResourceAsFile("SqlMapConfig.xml");
			
			FileOutputStream fout = new FileOutputStream(sqlMapConfigFile.getParent()+File.separator+"sqlMap.xml");
			final String sqlMapping = sb.toString();
			fout.write(sqlMapping.getBytes());
			fout.flush();
			fout.close();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();
			DocumentBuilder ibatisConfigBuilder = factory.newDocumentBuilder();
			InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
			Document doc = ibatisConfigBuilder.parse(inputStream);
			
			NodeList sqlMapElem = doc.getElementsByTagName("sqlMapConfig");
			
			Element elem = doc.createElement("sqlMap");
			elem.setAttribute("resource", "sqlMap.xml");
			sqlMapElem.item(0).appendChild(elem);
			
			Transformer trans = TransformerFactory.newInstance().newTransformer(); 
			File tempFile = File.createTempFile("sqlMapConfig", ".xml");
			tempFile.deleteOnExit();
			trans.transform(new DOMSource(doc),new StreamResult(tempFile));

			SqlMapClientHolder.sqlMapClient = SqlMapClientBuilder
					.buildSqlMapClient(new FileInputStream(tempFile));
			
		} catch (Exception e) {
			throw new RuntimeException("初始化Ibatis上下文失败！", e);
		}
	}

	public SqlMapClient getSqlMapClient() {
		transaction.set(new IbatisTransaction(SqlMapClientHolder.sqlMapClient));
		return SqlMapClientHolder.sqlMapClient;
	}
	
	public IbatisTransaction getIbatisTransaction(){
		return this.transaction.get();
	}
	
	public void closeSqlMapClient(){
		IbatisTransaction ibatisTransaction = this.getIbatisTransaction();
		if(ibatisTransaction != null){
			try {
				ibatisTransaction.end();
			} catch (Exception e) {
			}
			this.transaction.remove();
		}
	}
}
