package org.skiftsu;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RabbitQueue {
    public static final String TEXT_MESSAGE_QUEUE = "text_message";
    public static final String FILE_MESSAGE_QUEUE = "file_message";
    public static final String EMAIL_MESSAGE_QUEUE = "email_message";
    public static final String ANSWER_MESSAGE_QUEUE = "answer_message";

    @AllArgsConstructor
    @Getter
    public enum ERabbitQueue {
        Text(TEXT_MESSAGE_QUEUE),
        File(FILE_MESSAGE_QUEUE),
        Email(EMAIL_MESSAGE_QUEUE),
        Answer(ANSWER_MESSAGE_QUEUE);

        public final String queue;
    }
}

