package org.cms.entity;


import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * 投票用户类
 * 
*/
public class PollVoter extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1329069366249869361L;
	private int pollId;
	private int pollResultId;
	private String pollUser;
	private String pollIp;
	
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	public String getPollIp() {
		return pollIp;
	}
	public void setPollIp(String pollIp) {
		this.pollIp = pollIp;
	}
	public int getPollResultId() {
		return pollResultId;
	}
	public void setPollResultId(int pollResultId) {
		this.pollResultId = pollResultId;
	}
	public String getPollUser() {
		return pollUser;
	}
	public void setPollUser(String pollUser) {
		this.pollUser = pollUser;
	}
	
	
}
