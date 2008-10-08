package org.jrest4guice.persistence.ibatis;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import com.ibatis.sqlmap.client.SqlMapClient;

public class SqlMapClientBuilder {

	/**
	 * No instantiation allowed.
	 */
	protected SqlMapClientBuilder() {
	}

	/**
	 * Builds an SqlMapClient using the specified reader.
	 * 
	 * @param reader
	 *            A Reader instance that reads an sql-map-config.xml file. The
	 *            reader should read an well formed sql-map-config.xml file.
	 * @return An SqlMapClient instance.
	 */
	public static SqlMapClient buildSqlMapClient(Reader reader) {
		// return new XmlSqlMapClientBuilder().buildSqlMap(reader);
		return new InnerSqlMapConfigParser().parse(reader);
	}

	/**
	 * Builds an SqlMapClient using the specified reader and properties file.
	 * <p/>
	 * 
	 * @param reader
	 *            A Reader instance that reads an sql-map-config.xml file. The
	 *            reader should read an well formed sql-map-config.xml file.
	 * @param props
	 *            Properties to be used to provide values to dynamic property
	 *            tokens in the sql-map-config.xml configuration file. This
	 *            provides an easy way to achieve some level of programmatic
	 *            configuration.
	 * @return An SqlMapClient instance.
	 */
	public static SqlMapClient buildSqlMapClient(Reader reader, Properties props) {
		// return new XmlSqlMapClientBuilder().buildSqlMap(reader, props);
		return new InnerSqlMapConfigParser().parse(reader, props);
	}

	/**
	 * Builds an SqlMapClient using the specified input stream.
	 * 
	 * @param inputStream
	 *            An InputStream instance that reads an sql-map-config.xml file.
	 *            The stream should read a well formed sql-map-config.xml file.
	 * @return An SqlMapClient instance.
	 */
	public static SqlMapClient buildSqlMapClient(InputStream inputStream) {
		return new InnerSqlMapConfigParser().parse(inputStream);
	}

	/**
	 * Builds an SqlMapClient using the specified input stream and properties
	 * file. <p/>
	 * 
	 * @param inputStream
	 *            An InputStream instance that reads an sql-map-config.xml file.
	 *            The stream should read an well formed sql-map-config.xml file.
	 * @param props
	 *            Properties to be used to provide values to dynamic property
	 *            tokens in the sql-map-config.xml configuration file. This
	 *            provides an easy way to achieve some level of programmatic
	 *            configuration.
	 * @return An SqlMapClient instance.
	 */
	public static SqlMapClient buildSqlMapClient(InputStream inputStream,
			Properties props) {
		return new InnerSqlMapConfigParser().parse(inputStream, props);
	}
}
