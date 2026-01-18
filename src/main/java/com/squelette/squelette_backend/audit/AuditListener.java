package com.squelette.squelette_backend.audit;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditListener {

    private static AuditService auditService;

    @Autowired
    public void setAuditService(AuditService auditService) {
        AuditListener.auditService = auditService;
    }

    @PostPersist
    public void postPersist(final Object entity) {
        logAuditAction(entity, AuditAction.AJOUT, null, entity);
    }

    @PostUpdate
    public void postUpdate(final Object entity) {
        Object before = null;
        if (entity instanceof jakarta.persistence.EntityListeners) {
            // Get snapshot if available
            // This would require additional setup to capture the "before" state
        }
        logAuditAction(entity, AuditAction.MODIFICATION, before, entity);
    }

    @PostRemove
    public void postRemove(final Object entity) {
        Object before = entity;
        logAuditAction(entity, AuditAction.SUPPRESSION, before, null);
    }

    private void logAuditAction(Object entity, AuditAction action, Object before, Object after) {
        if (auditService != null) {
            try {
                auditService.logAuditDatabase(entity, action, before, after);
            } catch (Exception e) {
                log.error("Erreur lors de la lecture de l'opération pour sauvegarder dans la table d'audit", e);
            }
        }
    }
}
