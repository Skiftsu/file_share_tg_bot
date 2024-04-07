package org.skiftsu.repository;

import org.skiftsu.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findByTelegramUserId(Long tgId);
    Optional<AppUserEntity> findByEmail(String email);
}
