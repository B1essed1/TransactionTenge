package uz.tenge.transactiontenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import uz.tenge.transactiontenge.data.H2HKafkaPayload;
import uz.tenge.transactiontenge.data.H2HResponse;
import uz.tenge.transactiontenge.data.TOPICS;

@Service
public class HumoService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public HumoService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = TOPICS.HUMO2HUMO, groupId = "my-group")
    public void listen(H2HKafkaPayload message) {
        // todo some work with humo itself
        var response  = H2HResponse.create(message.getRrn());
        send(TOPICS.HUMO2HUMO_RES, response);

    }

    public <T> void send(String topic, T payload) {
        kafkaTemplate.send(topic, payload);
    }
}
