package mycamel.spring.boot.hawtio.service;

import lombok.RequiredArgsConstructor;
import mycamel.spring.boot.hawtio.dto.ApplicationProfile;
import mycamel.spring.boot.hawtio.dto.ConfigData;
import mycamel.spring.boot.hawtio.dto.ConfigurationProperty;
import mycamel.spring.boot.hawtio.entity.ConfigDataEntity;
import mycamel.spring.boot.hawtio.entity.ConfigDataEntityKey;
import mycamel.spring.boot.hawtio.repository.ConfigDataRepository;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigDataService {

	private final ConfigDataRepository configDataRepository;

	private final DataSource dataSource;

	@Transactional
	public void createAll(final List<ConfigData> configData) {
		this.saveAll(configData);
	}

	@Transactional
	public void recreateAll(final List<ConfigData> configData) {
		this.deleteProfiles(configData);
		this.saveAll(configData);
	}

	@Transactional
	public List<ConfigData> recreateAllAndFindAll(final List<ConfigData> configData) {
		this.deleteProfiles(configData);
		this.saveAll(configData);
		return findAll();
	}

	public List<ConfigData> findAll() {
		List<ConfigDataEntity> configDataEntities = this.configDataRepository.findAll();
		List<ConfigData> configDataList = new ArrayList<>();
		configDataEntities.stream()
			.collect(Collectors.groupingBy(
					c -> ConfigDataEntityKey.builder()
						.application(c.getApplication())
						.profile(c.getProfile())
						.label(c.getLabel())
						.build(),
					TreeMap::new,
					Collectors.mapping(e -> ConfigurationProperty.builder()
						.propKey(e.getPropKey())
						.propValue(e.getPropValue())
						.build(), Collectors.toList())))
			.forEach((k,
					v) -> configDataList.add(ConfigData.builder()
						.key(String.format("%s,%s", k.getApplication(), k.getProfile()))
						.application(k.getApplication())
						.profile(k.getProfile())
						.label(k.getLabel())
						.props(v)
						.build()));
		return configDataList;

	}

	public List<ConfigData> loadSql(final String sql) throws SQLException {
		ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());
		scriptRunner.setSendFullScript(false);
		scriptRunner.setStopOnError(true);
		scriptRunner.runScript(new StringReader((sql)));
		return this.findAll();
	}

	@Transactional
	public List<ConfigData> deleteApplicationProfiles(final List<ApplicationProfile> applicationProfiles) {
		applicationProfiles
			.forEach(a -> configDataRepository.deleteApplicationProfile(a.getApplication(), a.getProfile()));
		return this.findAll();
	}

	private void deleteProfiles(final List<ConfigData> configData) {
		List<ApplicationProfile> applicationProfiles = configData.stream()
			.map(config -> createApplicationProfile(config))
			.collect(Collectors.toUnmodifiableList());
		applicationProfiles
			.forEach(a -> configDataRepository.deleteApplicationProfile(a.getApplication(), a.getProfile()));
	}

	private void saveAll(final List<ConfigData> configData) {
		List<ConfigDataEntity> configDateEntries = configData.stream()
			.flatMap(config -> config.getProps().stream().map(prop -> createConfigDataEntity(config, prop)))
			.collect(Collectors.toUnmodifiableList());
		this.configDataRepository.saveAll(configDateEntries);
	}

	private ConfigDataEntity createConfigDataEntity(ConfigData configData, ConfigurationProperty prop) {
		return ConfigDataEntity.builder()
			.application(configData.getApplication())
			.profile(configData.getProfile())
			.label(configData.getLabel())
			.propKey(prop.getPropKey())
			.propValue(prop.getPropValue())
			.build();
	}

	private ApplicationProfile createApplicationProfile(ConfigData configData) {
		return ApplicationProfile.builder()
			.application(configData.getApplication())
			.profile(configData.getProfile())
			.build();
	}

}
