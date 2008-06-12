package org.jrest4guice.filemgr.service;

import java.io.File;

import org.jrest4guice.filemgr.entity.Connector;

public interface FileManagerService {
    
    Connector upload(File file, String fileName, String contentType, String fileType, String directory);

  
    Connector createFolder(String currentFolder, String newFolderName, String fileType);

    Connector getFoldersAndFiles(String currentFolder, String type);

    Connector getFolders(String currentFolder, String type);
}