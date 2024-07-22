package mycamel.spring.boot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mycamel.spring.boot.camel.dto.CamelRequest;
import mycamel.spring.boot.camel.dto.RefreshConfigRequest;
import mycamel.spring.boot.camel.support.CamelUtils;
import mycamel.spring.boot.camel.support.ValidCamelRequest;

@RestController
@RequestMapping(path = "/api/refreshConfig")
@Slf4j
@RequiredArgsConstructor
public class RefreshConfigController {

	private final CamelUtils camelUtils;

	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<String> refreshConfig(@ValidCamelRequest(endpoint = "RefreshConfig",
			type = RefreshConfigRequest.class,
			logSubject = "MyCamel:API_REQUEST:RefreshConfig (Enter)") CamelRequest<RefreshConfigRequest> camelRequest) {
		// public ResponseEntity<String> refreshConfig(@RequestBody RefreshConfigRequest
		// camelRequest) {
		// public ResponseEntity<Map<String, Set<String>>>
		// refreshConfig(@ValidCamelRequest(endpoint="RefreshConfig", type=
		// RefreshConfigRequest.class, logSubject = "RefreshConfig:API_REQUEST
		// (Enter)")CamelRequest<RefreshConfigRequest> camelRequest) {
		// return camelUtils.processCamelRequest(camelRequest, "direct:refresh-config");
		return ResponseEntity.ok("{\"a\":\"b\"}");
	}

}
