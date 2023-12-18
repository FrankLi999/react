package com.example.camel.dashboard.repository.mvc;

//import java.util.List;
//
//import net.bytebuddy.TypeCache;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import com.example.camel.dashboard.entity.r2dbc.com.example.camel.dashboard.entity.mvc.ConfigDataEntity;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;

// @Repository
public interface ConfigDataRepository { // extends CrudRepository<com.example.camel.dashboard.entity.mvc.ConfigDataEntity, Long> {
//    @Query(value="SELECT * FROM S2I_INTEGRATOR_PROPERTIES p ORDER BY p.APPLICATION asc, p.PROFILE asc, p.LABEL asc, p.PROP_KEY asc", nativeQuery = true)
//    List<com.example.camel.dashboard.entity.mvc.ConfigDataEntity> findAll();
//
//    @Query("SELECT c FROM com.example.camel.dashboard.entity.mvc.ConfigDataEntity c WHERE c.application = :application ")
//    List<com.example.camel.dashboard.entity.mvc.ConfigDataEntity> findConfigData(@Param("application") String application);
//
//    // @Modifying
//    // @Query("DELETE FROM APP_PROPERTIES WHERE application = :application and profile = :profile and label = :label and PROP_KEY = :propKey")
//    // Mono<Void> deletePropertyValue(String application, String profile, String label, String propKey);
//
//    @Modifying
//    @Query("DELETE FROM com.example.camel.dashboard.entity.mvc.ConfigDataEntity c WHERE c.application = :application and c.profile = :profile")
//    void deleteApplicationProfile(@Param("application") String application, @Param("profile") String profile);

    // @Modifying
    // @Query("UPDATE APP_PROPERTIES SET PROP_VALUE = :propValue WHERE application = :application and profile = :profile and label = :label and PROP_KEY = :propKey")
    // Mono<Void> updatePropertyValue(String application, String profile, String label, String propKey, String propValue);
}
