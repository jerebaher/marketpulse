package org.marketpulse.config.kafka;

import lombok.AllArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.marketpulse.stockdata.model.StockData;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@AllArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;

    /**
     * Creates a ConsumerFactory for a specific message type.
     * @param <T> The type of message being consumed
     * @param messageType The class of the message type
     * @return A ConsumerFactory for the specified message type
     */
    public <T> ConsumerFactory<String, T> consumerFactory(Class<T> messageType) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());

        // Create JsonDeserializer with custom ObjectMapper that has JavaTimeModule registered
        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(messageType, objectMapper);
        // Trust all packages - adjust this based on your security requirements
        jsonDeserializer.addTrustedPackages("*");
        
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            jsonDeserializer
        );
    }

    /**
     * Creates a generic consumer factory for String, Object.
     * @return A ConsumerFactory for String keys and Object values
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return consumerFactory(Object.class);
    }

    /**
     * Creates a KafkaListenerContainerFactory for a specific message type.
     * @param <T> The type of message being consumed
     * @param messageType The class of the message type
     * @return A KafkaListenerContainerFactory for the specified message type
     */
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> messageType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(messageType));
        return factory;
    }

    /**
     * Creates a generic KafkaListenerContainerFactory for String, Object.
     * @return A KafkaListenerContainerFactory for String keys and Object values
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(Object.class);
    }

    /**
     * Creates a specific KafkaListenerContainerFactory for StockData.
     * @return A KafkaListenerContainerFactory for String keys and StockData values
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockData> stockDataKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(StockData.class);
    }
}
