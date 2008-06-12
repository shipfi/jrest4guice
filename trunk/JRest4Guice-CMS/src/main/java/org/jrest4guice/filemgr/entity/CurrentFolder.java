package org.jrest4guice.filemgr.entity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentFolder {
    @XmlAttribute()
    private String path;

    @XmlAttribute()
    private String url;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url != null && !url.endsWith("/")) {
            url += "/";
        }

        this.url = url;
    }
}
