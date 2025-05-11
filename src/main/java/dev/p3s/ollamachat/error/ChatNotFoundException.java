package dev.p3s.ollamachat.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException() {
        super();
    }

    public ChatNotFoundException(String message) {
        super(message);
    }

    public ChatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ChatNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
