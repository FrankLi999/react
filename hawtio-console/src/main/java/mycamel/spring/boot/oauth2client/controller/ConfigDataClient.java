package mycamel.spring.boot.oauth2client.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import mycamel.spring.boot.resource.dto.ApplicationProfile;
import mycamel.spring.boot.resource.dto.ConfigData;

@FeignClient(name = "ConfigData", url = "${my.openfeign.client.api.url}")
public interface ConfigDataClient {

	@GetMapping(path = "configurations", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ConfigData> findAll();

	@PostMapping(path = "api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void post(@RequestBody List<ConfigData> configData);

	@PostMapping(path = "json", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ConfigData> uploadJson(@RequestBody List<ConfigData> configData)
			throws IOException, JsonProcessingException;

	@PostMapping(path = "sql", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ConfigData> losqSql(@RequestBody String sql) throws IOException, SQLException;

	@PutMapping(path = "configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void put(@RequestBody List<ConfigData> configData);

	@DeleteMapping(path = "configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles);

}
