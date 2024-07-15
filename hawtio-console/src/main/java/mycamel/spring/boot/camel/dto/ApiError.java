package mycamel.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class ApiError {

	private ResponseHeader responseHeader;

	private String traceId;

}
