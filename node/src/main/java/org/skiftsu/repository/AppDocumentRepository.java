package org.skiftsu.repository;

import org.skiftsu.entity.AppDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppDocumentRepository extends JpaRepository<AppDocumentEntity, Long> {
}
