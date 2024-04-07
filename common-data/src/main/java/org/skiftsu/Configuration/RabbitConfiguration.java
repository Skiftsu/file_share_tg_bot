package org.skiftsu.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.skiftsu.RabbitQueue.*;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() { return new Queue(TEXT_MESSAGE_QUEUE); }

    @Bean
    public Queue fileMessageQueue() {
        return new Queue(FILE_MESSAGE_QUEUE);
    }

    @Bean
    public Queue emailMessageQueue() {
        return new Queue(EMAIL_MESSAGE_QUEUE);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_MESSAGE_QUEUE);
    }
}
