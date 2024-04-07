package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONObject;
import org.skiftsu.dto.FileDto;
import org.skiftsu.service.interfaces.TGFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@CommonsLog
public class TGFileServiceImpl implements TGFileService {
    @Value("${bot.token}")
    private String token;

    @Override
    public FileDto getDocumentFile(Message msg) {
        var document = msg.getDocument();
        return getUrlByFileID(document.getFileId(), document.getFileSize());
    }

    @Override
    public FileDto getPhotoFile(Message msg) {
        var photo = msg.getPhoto().get(msg.getPhoto().size()-1);
        return getUrlByFileID(photo.getFileId(), photo.getFileSize().longValue());
    }

    /** Returns the path to a file in file system*/
    private FileDto getUrlByFileID(String fileId, Long fileSize){
        try {
            /* Get file path */
            String url = String.format("https://api.telegram.org/bot%s/getFile?file_id=%s", token, fileId);
            ResponseEntity<String> response = new RestTemplate().getForEntity(url, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("Bad response from telegram service: " + response);
            }
            var filePath = String.valueOf(new JSONObject(response.getBody())
                    .getJSONObject("result")
                    .getString("file_path"));

            /* Get file name */
            int lastIndex = filePath.lastIndexOf('/');
            String fileName = "default.jpg";
            String fileType = "error";
            if (lastIndex != -1) {
                fileName = filePath.substring(lastIndex + 1);
                fileType = filePath.substring(0, lastIndex-1);
            }

            var fileUrl = String.format("https://api.telegram.org/file/bot%s/%s", token, filePath);
            return new FileDto(fileUrl, fileName, fileSize, fileType);
        } catch (Exception e) {
            log.error("Error message: " + e.getMessage());
        }
        // TODO Сделать исключение
        return null;
    }
}
