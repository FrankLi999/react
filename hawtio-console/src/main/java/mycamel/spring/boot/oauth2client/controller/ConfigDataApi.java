
package mycamel.spring.boot.oauth2client.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import mycamel.spring.boot.resource.dto.ApplicationProfile;
import mycamel.spring.boot.resource.dto.ConfigData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spring/admin/api")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ConfigDataApi {

	private final ConfigDataClient configDataClient;

	private final ObjectMapper objectMapper;

	@GetMapping(path = "configurations", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<ConfigData> findAll() {
		return configDataClient.findAll();
	}

	@PostMapping(path = "api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void post(@RequestBody List<ConfigData> configData) {
		configDataClient.post(configData);
	}

	@PostMapping(path = "json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<ConfigData> uploadJson(@RequestParam("file") MultipartFile file)
			throws IOException, JsonProcessingException {
		List<ConfigData> configData = objectMapper.readValue(file.getInputStream(),
				new TypeReference<List<ConfigData>>() {
				});
		return configDataClient.uploadJson(configData);
	}

	@PostMapping(path = "sql", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<ConfigData> losqSql(@RequestParam("file") MultipartFile file) throws IOException, SQLException {
		String sql = new String(file.getBytes());
		return configDataClient.losqSql(sql);
	}

	@PutMapping(path = "configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void put(@RequestBody List<ConfigData> configData) {
		configDataClient.put(configData);
	}

	@DeleteMapping(path = "configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles) {
		return configDataClient.deleteApplicationProfiles(applicationProfiles);
	}

	/**
	 * @param token
	 * @return response like, * { "token":
	 * "d-lp0ZL5WLrtOMzo7_xzBnEkpHh8aL6uW0EBjPcyaHDjSasETttc5fPBYI7ADaqJjtFHNhQViRpLXo-Da3gw6sFTDhHVeZlh",
	 * "parameterName": "_csrf", "headerName": "X-CSRF-TOKEN" }
	 *
	 *
	 * import axios from 'axios';
	 *
	 * function post(path, data) { return axios.get("/csrf") .then(tokenResp => { let
	 * config = { headers: { 'X-CSRF-TOKEN': tokenResp.data.token, } } return
	 * axios.post(path, data, config); }) .then(res => res.data) }
	 */
	@PostMapping("csrf")
	public CsrfToken csrf(CsrfToken token) {
		return token;
	}

}
