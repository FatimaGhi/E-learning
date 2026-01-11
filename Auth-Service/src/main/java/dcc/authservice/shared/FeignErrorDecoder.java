package dcc.authservice.shared;

import feign.codec.ErrorDecoder;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = extractErrorMessage(response);

        return switch (response.status()) {
            case 400 -> CustomResponseException.BadRequest(
                    "Student Service - Bad Request: " + errorMessage);
            case 404 -> CustomResponseException.ResourceNotFound(
                    "Student Service - Resource Not Found: " + errorMessage);
            case 409 -> CustomResponseException.Conflict(
                    "Student Service - Conflict: " + errorMessage);
            case 500 -> CustomResponseException.InternalError(
                    "Student Service - Internal Error: " + errorMessage);
            default -> CustomResponseException.InternalError(
                    "Student Service - Unknown Error: " + errorMessage);
        };
    }

    private String extractErrorMessage(Response response) {
        try {
            if (response.body() != null) {
                InputStream inputStream = response.body().asInputStream();
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Error reading response body", e);
        }
        return "Unknown error occurred";
    }
}
