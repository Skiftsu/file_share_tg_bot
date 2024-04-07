package org.skiftsu.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.skiftsu.service.enums.EUserState;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "app_user")
public class AppUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long telegramUserId;
    String username;
    String email;
    Boolean isActive;
    @Enumerated(EnumType.STRING)
    EUserState state;
    String additionalInfo;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileOwner")
    List<AppDocumentEntity> userFiles;

    @CreationTimestamp
    LocalDateTime firstLoginDate;
}
