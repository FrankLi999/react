package io.hawt.example.spring.boot;

import java.awt.*;
import java.util.Map;
import java.util.Set;

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

	private final ProducerTemplate producerTemplate;

	@GetMapping(value = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<Map<String, Set<String>>> refreshConfig() {
		return null;
	}

}
