package com.squelette.squelette_backend.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "audit_entity")
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "username")
    private String username;

    @Column(name = "audit_type")
    @Enumerated(EnumType.STRING)
    private AuditType auditType;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "snapshot_json", columnDefinition = "jsonb")
    @Transient
    @JsonIgnore
    private JsonNode snapshotJson;

    @PostLoad
    public void saveSnapshot() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.snapshotJson = mapper.valueToTree(this);
        } catch (final Exception e) {
            log.error("Erreur lors de la sérialisation snapshot de {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}

