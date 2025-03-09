package org.marketpulse.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "stock-prices", groupId = "data-processor-group")
    public void listen(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        System.out.println("Mensaje recibido:");
        System.out.println("Clave: " + key);
        System.out.println("Valor: " + value);

        procesarPrecioDeAccion(value);
    }

    private void procesarPrecioDeAccion(String mensaje) {
        System.out.println("Procesando el precio de la acción: " + mensaje);
    }
}
