package org.skiftsu.service.interfaces;

import org.skiftsu.dto.FileDto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TGFileService {
    FileDto getDocumentFile(Message msg);
    FileDto getPhotoFile(Message msg);
}
