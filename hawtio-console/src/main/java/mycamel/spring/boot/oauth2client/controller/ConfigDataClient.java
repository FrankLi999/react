package mycamel.spring.boot.oauth2client.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import feign.Headers;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import mycamel.spring.boot.resource.dto.ApplicationProfile;
import mycamel.spring.boot.resource.dto.ConfigData;

// @FeignClient(name = "ConfigData", url = "${my-camel.openfeign.client.api.url}")
public interface ConfigDataClient {

	// @GetMapping(path = "configurations", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestLine("GET")
	@Headers({ "produces: application/json" })
	public List<ConfigData> findAll();

	// @PostMapping(path = "api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	@RequestLine("POST")
	@Headers({ "consumes: application/json" })
	public void post(@RequestBody List<ConfigData> configData);

//	@PostMapping(path = "json", consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestLine("POST")
	@Headers({ "consumes: application/json", "produces: application/json" })
	public List<ConfigData> uploadJson(@RequestBody List<ConfigData> configData)
			throws IOException, JsonProcessingException;

	// @PostMapping(path = "sql", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestLine("POST")
	@Headers({ "consumes: text/html", "produces: application/json" })
	public List<ConfigData> losqSql(@RequestBody String sql) throws IOException, SQLException;

	// @PutMapping(path = "configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	@RequestLine("PUT")
	@Headers({ "consumes: application/json"})
	public void put(@RequestBody List<ConfigData> configData);

	// @DeleteMapping(path = "configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	@RequestLine("DELETE")
	@Headers({ "consumes: application/json"})
	public List<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles);

}
