package org.jrest4guice.filemgr.service;

import com.google.inject.ImplementedBy;



public interface FileConfiguration {
   
    String getFileServletDir();

    String getFileServletPrefix();

    String[] getFileUploadAllowedContentTypes();

    boolean isCreateFolderAllowed();
}
