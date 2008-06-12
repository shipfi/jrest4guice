package org.jrest4guice.filemgr.entity;

import java.io.Serializable;

public class UploadResult implements Serializable {
    private static final long serialVersionUID = 1;
    private String modifiedFileName;
    private String fileURL;
    private int resultCode;
    private java.io.File file;

    public UploadResult() {
    }

    public UploadResult(int resultCode) {
        this.modifiedFileName = null;
        this.fileURL = null;
        this.resultCode = resultCode;
    }

    public UploadResult(String modifiedFileName, String fileURL, boolean fileNameModified, java.io.File file) {
        this.modifiedFileName = modifiedFileName;
        this.fileURL = fileURL;
        this.resultCode = fileNameModified ? 201 : 0;
        this.file = file;
    }

    public String getModifiedFileName() {
        return modifiedFileName;
    }

    public void setModifiedFileName(String modifiedFileName) {
        this.modifiedFileName = modifiedFileName;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public java.io.File getFile() {
        return file;
    }

    public void setFile(java.io.File file) {
        this.file = file;
    }
}