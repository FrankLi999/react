package mycamel.spring.boot.hawtio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// @Jacksonized
public class ApplicationProfile {

	private String application;

	private String profile;

}