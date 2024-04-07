package org.skiftsu.service.impl;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@CommonsLog
class MainServiceImplTest {
    @Autowired
    private RawDataRepository rawDataDAO;

    @Test
    public void testSaveRawData() {
        Update update = new Update();
        Message message = new Message();
        message.setText("Test message");
        update.setMessage(message);

        RawDataEntity rawData = RawDataEntity.builder().event(update).build();

        Set<RawDataEntity> testData = new HashSet<>();
        testData.add(rawData);
        System.out.println("Hash before save: " + rawData.hashCode());

        rawDataDAO.save(rawData);
        System.out.println("Hash after save: " + rawData.hashCode());

        assertTrue(testData.contains(rawData), "Entity not found in the set");
    }
}