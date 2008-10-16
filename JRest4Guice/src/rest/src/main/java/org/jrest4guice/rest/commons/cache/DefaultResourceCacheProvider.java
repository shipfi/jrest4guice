package org.jrest4guice.rest.commons.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.jrest4guice.persistence.hibernate.HibernateEventListener;
import org.jrest4guice.rest.RestContextManager;

@SuppressWarnings("serial")
public class DefaultResourceCacheProvider implements ResourceCacheProvider {
	private static Log log = LogFactory.getLog(DefaultResourceCacheProvider.class);
	
	public DefaultResourceCacheProvider(){
		//添加Hibernate事件监听器，来实现资源缓存的更新
		HibernateEventListener.getInstance().addPostUpdateEventListener(new PostUpdateEventListener(){
			@Override
			public void onPostUpdate(PostUpdateEvent event) {
				DefaultResourceCacheProvider.this.clearStaticResouceCache(event.getId().toString(),RestContextManager.getRequest());
			}
		});
		HibernateEventListener.getInstance().addPostDeleteEventListener(new PostDeleteEventListener(){
			@Override
			public void onPostDelete(PostDeleteEvent event) {
				DefaultResourceCacheProvider.this.clearStaticResouceCache(event.getId().toString(),RestContextManager.getRequest());
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.rest.cache.ResourceCacheProvider#cacheStaticResource(
	 * java.lang.String, java.lang.String, byte[],
	 * javax.servlet.http.HttpServletRequest)
	 */
	public void cacheStaticResource(String uri, String mimeType,
			byte[] content, HttpServletRequest request) {
		uri = uri.replaceAll("/", ".");
		mimeType = mimeType.replaceAll("/", ".");
		String resourceUrl = ResourceCacheManager.getInstance()
				.getCacheStorePath()
				+ "/" + uri + "." + mimeType;
		File file = new File(request.getSession().getServletContext()
				.getRealPath(resourceUrl));
		File parentFile = file.getParentFile();
		if(parentFile != null && !parentFile.exists())
			parentFile.mkdirs();
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(content);
			fout.flush();
		} catch (Exception e) {
			log.error("cacheStaticResource 失败：",e);
		} finally {
			try {
				if (fout != null)
					fout.close();
			} catch (IOException e) {
				log.error("cacheStaticResource 关闭文件失败：",e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.rest.cache.ResourceCacheProvider#findStaticCacheResource
	 * (java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpServletRequest)
	 */
	public String findStaticCacheResource(String url, String mimeType,
			HttpServletRequest request) {
		url = url.replaceAll("/", ".");
		mimeType = mimeType.replaceAll("/", ".");
		String resourceUrl = ResourceCacheManager.getInstance()
				.getCacheStorePath()
				+ "/" + url + "." + mimeType;
		File file = new File(request.getSession().getServletContext()
				.getRealPath(resourceUrl));
		if (file.exists()) {
			if (!resourceUrl.startsWith("/"))
				resourceUrl = "/" + resourceUrl;
			return resourceUrl;
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jrest4guice.rest.cache.ResourceCacheProvider#clearStaticResouceCache
	 * (java.lang.String)
	 */
	public void clearStaticResouceCache(String resourceId,
			HttpServletRequest request) {
		final HttpSession session = request.getSession();
		try {
			File cacheDirectory = new File(session.getServletContext()
					.getRealPath(
							ResourceCacheManager.getInstance().getCacheStorePath()));
			File[] files = cacheDirectory.listFiles();
			if(files == null)
				return;
			
			String key,fName;
			for(File file :files){
				fName = file.getName();
				if(fName.indexOf(resourceId) != -1){
					key = ResourceCacheProvider.ETAGS_SESSION_KEY+fName;
					session.removeAttribute(key);
					file.delete();
				}
			}
		} catch (Exception e) {
			log.error("clearStaticResouceCache 失败：",e);
		}
	}
}
