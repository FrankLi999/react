package com.bpwizard.configjdbc.core.security.userstore.repository;

import com.bpwizard.configjdbc.core.security.userstore.entity.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends RefreshableCRUDRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
}