package org.jrest4guice.rest.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.jrest4guice.jpa.HibernateEventListener;
import org.jrest4guice.rest.context.HttpContextManager;

@SuppressWarnings("serial")
public class DefaultResourceCacheProvider implements ResourceCacheProvider {
	
	public DefaultResourceCacheProvider(){
		HibernateEventListener.getInstance().addPostUpdateEventListener(new PostUpdateEventListener(){
			@Override
			public void onPostUpdate(PostUpdateEvent event) {
				DefaultResourceCacheProvider.this.clearStaticResouceCache(event.getId().toString(),HttpContextManager.getRequest());
			}
		});
		HibernateEventListener.getInstance().addPostDeleteEventListener(new PostDeleteEventListener(){
			@Override
			public void onPostDelete(PostDeleteEvent event) {
				DefaultResourceCacheProvider.this.clearStaticResouceCache(event.getId().toString(),HttpContextManager.getRequest());
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
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(content);
			fout.flush();
		} catch (Exception e) {
		} finally {
			try {
				if (fout != null)
					fout.close();
			} catch (IOException e) {
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
		File cacheDirectory = new File(request.getSession().getServletContext()
				.getRealPath(
						ResourceCacheManager.getInstance().getCacheStorePath()));
		File[] files = cacheDirectory.listFiles();
		if(files == null)
			return;
		
		for(File file :files){
			if(file.getName().indexOf(resourceId) != -1)
				file.delete();
		}
	}
}
