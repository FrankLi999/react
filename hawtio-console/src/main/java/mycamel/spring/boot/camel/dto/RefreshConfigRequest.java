package mycamel.spring.boot.camel.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshConfigRequest {

	private RequestHeader requestHeader;

	private List<RefreshConfig> refreshConfigurations;

}
