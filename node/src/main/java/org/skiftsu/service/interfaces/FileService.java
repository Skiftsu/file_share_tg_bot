package org.skiftsu.service.interfaces;

import org.skiftsu.dto.FileDto;

import java.io.IOException;

public interface FileService {
    /** Returns the path to a file in file system*/
    String saveFile(FileDto file, String folderName);
}
