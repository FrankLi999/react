package com.bpwizard.configjdbc.configdata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.bpwizard.configjdbc.configdata.entity.ConfigDataEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigDataRepository extends CrudRepository<ConfigDataEntity, Long> {
   @Query(value="SELECT * FROM APP_PROPERTIES p ORDER BY p.APPLICATION asc, p.PROFILE asc, p.LABEL asc, p.PROP_KEY asc", nativeQuery = true)
   List<ConfigDataEntity> findAll();

   @Query("SELECT c FROM ConfigDataEntity c WHERE c.application = :application ")
   List<ConfigDataEntity> findConfigData(@Param("application") String application);

   // @Modifying
   // @Query("DELETE FROM APP_PROPERTIES WHERE application = :application and profile = :profile and label = :label and PROP_KEY = :propKey")
   // Mono<Void> deletePropertyValue(String application, String profile, String label, String propKey);

   @Modifying
   @Query("DELETE FROM ConfigDataEntity c WHERE c.application = :application and c.profile = :profile")
   void deleteApplicationProfile(@Param("application") String application, @Param("profile") String profile);

    @Modifying
    @Query(value="UPDATE APP_PROPERTIES SET PROP_VALUE = :propValue WHERE application = :application and profile = :profile and label = :label and PROP_KEY = :propKey", nativeQuery = true)
    void updatePropertyValue(String application, String profile, String label, String propKey, String propValue);
}
