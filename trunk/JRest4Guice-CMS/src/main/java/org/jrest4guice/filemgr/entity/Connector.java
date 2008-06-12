package org.jrest4guice.filemgr.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Connector")
@XmlAccessorType(XmlAccessType.FIELD)
public class Connector {
    @XmlElement(name = "CurrentFolder")
    private CurrentFolder currentFolder;

    @XmlElementWrapper(name = "Folders")
    @XmlElement(name = "Folder")
    private List<Folder> folders;

    @XmlElementWrapper(name = "Files")
    @XmlElement(name = "File")
    private List<File> files;

    @XmlElement(name = "Error")
    private Error error;

    @XmlElement(name = "UploadResult")
    private UploadResult uploadResult;

    @XmlAttribute()
    private String command;

    @XmlAttribute()
    private String resourceType;

    public CurrentFolder getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(CurrentFolder currentFolder) {
        this.currentFolder = currentFolder;
    }

    public List<Folder> getFolders() {
        if (folders == null) {
            folders = new ArrayList<Folder>();
        }
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<File> getFiles() {
        if (files == null) {
            files = new ArrayList<File>();
        }
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean isError() {
        return error != null;
    }

    public UploadResult getUploadResult() {
        return uploadResult;
    }

    public void setUploadResult(UploadResult uploadResult) {
        this.uploadResult = uploadResult;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
