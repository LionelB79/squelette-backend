package com.squelette.squelette_backend.audit;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditCleanupScheduler {

    private final AuditService auditService;

    // Configuration à adapter selon vos besoins
    private static final int DATABASE_RETENTION_DAYS = 90; // 3 mois
    private static final int API_RETENTION_DAYS = 30;      // 1 mois

    /**
     * Purge des audits désactivés
     * Exécution tous les jours à 2h du matin
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void purgeOldAuditsScheduled() {
        log.debug("[AUDIT] Purge des audits désactivés");

        try {
            // Purge des audits BDD de plus de DATABASE_RETENTION_DAYS jours
            auditService.purgeOldDatabaseAudits(DATABASE_RETENTION_DAYS);
            log.info("[AUDIT] Purge des audits BDD de plus de {} jours", DATABASE_RETENTION_DAYS);
        } catch (Exception e) {
            log.error("[AUDIT] Erreur lors de la purge des audits BDD", e);
        }

        try {
            // Purge des audits API de plus de API_RETENTION_DAYS jours
            auditService.purgeOldApiAudits(API_RETENTION_DAYS);
            log.info("[AUDIT] Purge des audits API de plus de {} jours", API_RETENTION_DAYS);
        } catch (Exception e) {
            log.error("[AUDIT] Erreur lors de la purge des audits API", e);
        }
    }
}

