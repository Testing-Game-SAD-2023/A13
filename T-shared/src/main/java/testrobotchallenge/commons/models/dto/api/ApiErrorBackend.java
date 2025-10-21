package testrobotchallenge.commons.models.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ApiErrorBackend {
    private List<Map<String, String>> errors;

    public ApiErrorBackend(String message) {
        errors = new ArrayList<>();
        errors.add(Map.of("message", message));
    }
}
