package org.marketpulse.stockdata.messaging;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
@Slf4j
public class StockDataProcessor {

    private final Deque<Double> priceQueue = new ArrayDeque<>();

    public void processMessage(String message) {
        try {
            double price = extractPriceFromMessage(message);

            addPrice(price);

            double movingAverage = calculateMovingAverage();
            log.info("Precio actual: {}, Promedio móvil: {}", price, movingAverage);

            if (price > movingAverage * 1.05) {
                log.warn("¡Alerta! El precio supera en un 5% el promedio móvil.");
            }

        } catch (Exception e) {
            log.error("Error procesando el mensaje: {}", message, e);
        }
    }


    private double extractPriceFromMessage(String message) {
        String priceStr = message.replaceAll(".*\"price\":\\s*(\\d+\\.?\\d*).*", "$1");
        return Double.parseDouble(priceStr);
    }

    private void addPrice(double price) {
        int movingAverageWindow = 5;
        if (priceQueue.size() >= movingAverageWindow) {
            priceQueue.pollFirst();
        }
        priceQueue.addLast(price);
    }

    private double calculateMovingAverage() {
        return priceQueue.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
