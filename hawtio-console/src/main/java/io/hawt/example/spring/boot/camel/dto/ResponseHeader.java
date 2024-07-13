package io.hawt.example.spring.boot.camel.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class ResponseHeader {

	private Boolean responseCode;

	@Valid
	List<ResponseMessage> responseMessages = new ArrayList<>();

}
