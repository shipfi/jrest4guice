package org.jrest4guice.persistence.jpa;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.inject.Singleton;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Singleton
public class EntityManagerFactoryHolder {
	private EntityManagerFactory entityManagerFactory;

	private final ThreadLocal<EntityManager> entityManager = new ThreadLocal<EntityManager>();

	public EntityManagerFactoryHolder() {
		//注意:这一个版本只实现对一个persistence-unit的支持
		try {
			Set<String> units = new PersistenceXmlReader().doParse();
			this.entityManagerFactory = Persistence
			.createEntityManagerFactory(units.toArray()[0].toString());
		} catch (Exception e) {
			throw new RuntimeException("初始化 EntityManagerFactory 失败",e);
		}
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFactory;
	}

	public EntityManager getEntityManager() {
		EntityManager em = this.entityManager.get();
		// 如果不存在，则创建一个新的
		if (em == null) {
			em = getEntityManagerFactory().createEntityManager();
			this.entityManager.set(em);
		}
		return em;
	}

	public void closeEntityManager() {
		EntityManager em = this.entityManager.get();
		if (em != null) {
			try {
				if (em.isOpen())
					em.close();
			} finally {
				this.entityManager.remove();
			}
		}
	}

	static class PersistenceXmlReader {
		class XmlHandler extends DefaultHandler {
			private Set<String> units;

			public XmlHandler() {
				this.units = new HashSet<String>();
			}

			public Set<String> getUnits() {
				return units;
			}

			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				if (qName.equalsIgnoreCase("persistence-unit")) {
					units.add(attributes.getValue("name").trim());
				}
			}
		}

		public Set<String> doParse() throws Exception {
			XmlHandler handler = new XmlHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();
			URL confURL = EntityManagerFactoryHolder.class.getClassLoader()
					.getResource("META-INF/persistence.xml");
			parser.parse(confURL.toString(), handler);
			
			return handler.getUnits();
		}
	}
}
