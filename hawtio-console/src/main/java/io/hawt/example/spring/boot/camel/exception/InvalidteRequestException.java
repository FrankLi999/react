package io.hawt.example.spring.boot.camel.exception;

public class InvalidteRequestException extends RuntimeException {
    private String endpointId;
    public InvalidteRequestException(String endpointId, String message, Throwable cause) {
        super(message, cause);
        this.endpointId = endpointId;
    }

    public String getEndpointId() {
        return endpointId;
    }
}
