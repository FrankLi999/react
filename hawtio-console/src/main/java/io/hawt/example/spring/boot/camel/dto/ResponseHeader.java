package io.hawt.example.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain=true)
@Builder
public class ResponseHeader {
    private Boolean responseCode;
    @Valid
    List<ResponseMessage> responseMessages = new ArrayList<>();
}
