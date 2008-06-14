package org.jrest4guice.remoteClient.sample.resource;

import org.jrest4guice.client.Page;
import org.jrest4guice.sample.contact.entity.Contact;

public class TestResult {
	private long duration;
	private Page<Contact> page;
	
	public TestResult(long duration, Page<Contact> page) {
		super();
		this.duration = duration;
		this.page = page;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public Page<Contact> getPage() {
		return page;
	}
	public void setPage(Page<Contact> page) {
		this.page = page;
	}
}
