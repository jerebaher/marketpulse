package org.marketpulse.common.exception;

public class KafkaMessageException extends RuntimeException {

    public KafkaMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
