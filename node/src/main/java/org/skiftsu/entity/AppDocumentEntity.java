package org.skiftsu.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "app_document")
public class AppDocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String tgUrl;
    String fsPath;
    String name;
    Long fileSize;
    String type;
    @CreationTimestamp
    LocalDateTime saveDate;

    @ManyToOne()
    AppUserEntity fileOwner;
}
