package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.dto.FileDto;
import org.skiftsu.service.interfaces.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CommonsLog
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String saveFile(FileDto file, String folderName){
        try {
            // Create folder for file
            File images_folder = new File("images");
            if (!images_folder.exists()) {
                images_folder.mkdir();
            }
            File folder = new File(images_folder.getAbsoluteFile() + File.separator + folderName);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String filePath = folder.getAbsolutePath() + File.separator + file.getName();

            // Download file
            URL urlObj = new URL(file.getUrl());
            InputStream in = urlObj.openStream();
            Path path = Paths.get(filePath);
            Files.copy(in, path);
            return filePath;
        } catch (Exception e) {
            log.error("Error: " + e);
        }
        // TODO сделать исключение
        return null;
    }
}
