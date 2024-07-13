package io.hawt.example.spring.boot;

import java.awt.*;
import java.util.Map;
import java.util.Set;

import io.hawt.example.spring.boot.camel.dto.CamelRequest;
import io.hawt.example.spring.boot.camel.dto.RefreshConfigRequest;
import io.hawt.example.spring.boot.camel.support.CamelUtils;
import io.hawt.example.spring.boot.camel.support.ValidCamelRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.camel.ProducerTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/my-camel/console/refreshConfig")
@Slf4j
@RequiredArgsConstructor
public class RefreshConfigController {
	private final CamelUtils camelUtils;
	@GetMapping(value = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> refreshConfig(@ValidCamelRequest(endpoint="RefreshConfig", type= RefreshConfigRequest.class,
			logSubject = "MyCamel:API_REQUEST:RefreshConfig (Enter)")CamelRequest<RefreshConfigRequest> camelRequest) {
	// public ResponseEntity<Map<String, Set<String>>> refreshConfig(@ValidCamelRequest(endpoint="RefreshConfig", type= RefreshConfigRequest.class, logSubject = "RefreshConfig:API_REQUEST (Enter)")CamelRequest<RefreshConfigRequest> camelRequest) {
        return camelUtils.processCamelRequest(camelRequest, "direct:refresh-config");
	}
}
