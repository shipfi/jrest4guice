package org.cms.entity;

import java.util.Date;
import java.util.Set;

import org.jrest4guice.jpa.audit.AuditableEntity;


/**
 * 投票类
 * 
*/
public class Poll  extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4374211470111014285L;
	private int topicId;
	private String content;
	private Date dateCreated;
	
	private Set pollResults;	
	public Set getPollResults() {
		return pollResults;
	}
	public void setPollResults(Set pollResults) {
		this.pollResults = pollResults;
	}
	
	public void addPollResult(PollResult pollResult) {
		pollResult.setPoll(this);
		pollResults.add(pollResult);
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	
	
}
