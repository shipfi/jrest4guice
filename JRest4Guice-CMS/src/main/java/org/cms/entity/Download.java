package org.cms.entity;

import java.util.Date;
import java.util.Set;

import org.cms.entity.DownClass;
import org.jrest4guice.jpa.audit.AuditableEntity;



public class Download extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8722574204403598710L;
	private int classId;
	private String title;
	private String filename;
	private String folder;
	private String filesize;
	private String filetype;
	private String description;
	private int hits;
	private int times;
	private String uploader;
	private int isVouch;
	private int isDemo;
	private String demoUrls;
	private String tags;
	private String os;
	private int language;
	private Date dateCreated;
	private String uuid;
	private int star;
	private int state;
	
	// 所属下载类别
	private DownClass downClass;
	public DownClass getDownClass() {
		return downClass;
	}
	public void setDownClass(DownClass downClass) {
		this.downClass = downClass;
	}
	
	// 下载评论 SET
	private Set downloadComments;	
	public Set getDownloadComments() {
		return downloadComments;
	}
	public void setDownloadComments(Set downloadComments) {
		this.downloadComments = downloadComments;
	}
	
	public void addDownloadComment(DownloadComment downloadComment) {
		downloadComment.setDownload(this);
		downloadComments.add(downloadComment);
	}
	
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classid) {
		this.classId = classid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public int getIsVouch() {
		return isVouch;
	}
	public void setIsVouch(int isVouch) {
		this.isVouch = isVouch;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getIsDemo() {
		return isDemo;
	}
	public void setIsDemo(int isDemo) {
		this.isDemo = isDemo;
	}
	public String getDemoUrls() {
		return demoUrls;
	}
	public void setDemoUrls(String demoUrls) {
		this.demoUrls = demoUrls;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	
}
