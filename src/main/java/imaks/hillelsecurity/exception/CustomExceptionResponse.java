package imaks.hillelsecurity.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomExceptionResponse {

    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
}
