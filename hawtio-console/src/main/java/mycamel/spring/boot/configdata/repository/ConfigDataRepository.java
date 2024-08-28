package mycamel.spring.boot.configdata.repository;

import mycamel.spring.boot.configdata.entity.ConfigDataEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigDataRepository extends CrudRepository<ConfigDataEntity, Long> {

	@Query(value = "SELECT * FROM MY_PROPERTIES p ORDER BY p.APPLICATION asc, p.PROFILE asc, p.LABEL asc, p.PROP_KEY asc",
			nativeQuery = true)
	List<ConfigDataEntity> findAll();

	@Query("SELECT c FROM ConfigDataEntity c WHERE c.application = :application ")
	List<ConfigDataEntity> findConfigData(@Param("application") String application);

	// @Modifying
	// @Query("DELETE FROM MY_PROPERTIES WHERE application = :application and profile =
	// :profile and label = :label and PROP_KEY = :propKey")
	// Mono<Void> deletePropertyValue(String application, String profile, String label,
	// String propKey);

	@Modifying
	@Query("DELETE FROM ConfigDataEntity c WHERE c.application = :application and c.profile = :profile")
	void deleteApplicationProfile(@Param("application") String application, @Param("profile") String profile);

	@Modifying
	@Query(value = "UPDATE MY_PROPERTIES SET PROP_VALUE = :propValue WHERE application = :application and profile = :profile and label = :label and PROP_KEY = :propKey",
			nativeQuery = true)
	void updatePropertyValue(String application, String profile, String label, String propKey, String propValue);

}
