package mycamel.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class CamelResponse<T> {

	RequestContext requestContext;

	private T responseBody;

}
