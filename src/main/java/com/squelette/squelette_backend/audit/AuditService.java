package com.squelette.squelette_backend.audit;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.lang.reflect.Field;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditDatabase(final Object entity, final AuditAction action, final Object before, Object after) {
        DataAuditDatabase dataAuditDatabase = DataAuditDatabase.builder()
                .primaryKeyName(getPrimaryKeyName(entity))
                .valueIdEntity(getIdEntityValue(entity))
                .tableName(getTableName(entity))
                .beforeValue(before == null ? null : objectMapper.valueToTree(before))
                .afterValue(after == null ? null : objectMapper.valueToTree(after))
                .build();

        createAuditEntity(dataAuditDatabase, AuditType.BDD, action);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditEndpointAccess(final String endpoint, final int status, final String httpMethod) {
        DataAuditApi dataAuditApi = DataAuditApi.builder()
                .url(endpoint)
                .status(status)
                .httpMethod(httpMethod)
                .build();

        createAuditEntity(dataAuditApi, AuditType.API, AuditAction.LECTURE);
    }

    /**
     * Créer un audit dans la BDD
     *
     * @param data       Données d'audit
     * @param auditType  Type d'audit
     * @param action     Action
     */
    private void createAuditEntity(Object data, AuditType auditType, AuditAction action) {
        AuditEntity auditTable = new AuditEntity();
        auditTable.setDate(LocalDateTime.now());
        auditTable.setUsername(getCurrentUser());
        auditTable.setAuditType(auditType);
        auditTable.setAction(action);

        entityManager.persist(auditTable);
    }

    @Transactional
    public void purgeOldDatabaseAudits(int retentionDays) {
        purgeAudits(AuditType.BDD, retentionDays);
    }

    @Transactional
    public void purgeOldApiAudits(int retentionDays) {
        purgeAudits(AuditType.API, retentionDays);
    }

    /**
     * Purge des audits
     *
     * @param auditType       Type d'audit
     * @param retentionDays   Rétention de la purge
     */
    private void purgeAudits(AuditType auditType, int retentionDays) {
        LocalDateTime limitDate = LocalDateTime.now().minusDays(retentionDays);
        deleteAudit(auditType, limitDate);

        log.info("Purge des audits {}: entités supprimées avant le: {}", auditType, limitDate);
    }

    /**
     * Exécution de la requête de purge des audits
     *
     * @param auditType   Type d'audit
     * @param limitDate   Date limite des audits à purger
     */
    private void deleteAudit(AuditType auditType, LocalDateTime limitDate) {
        Query query = entityManager.createQuery(
                "DELETE FROM AuditTable a WHERE a.auditType = :type AND a.date < :limitDate"
        );

        query.setParameter("type", auditType);
        query.setParameter("limitDate", limitDate);

        query.executeUpdate();
    }

    /**
     * Cette méthode retourne l'id current utilisateur connecté sinon SYSTEM
     *
     * @return String Id utilisateur connecté sinon SYSTEM
     */
    private String getCurrentUser() {
        // TODO: À adapter selon système d'authentification
        // Par exemple: return SecurityContextHolder.getContext().getAuthentication().getName();
        return "SYSTEM";
    }

    /**
     * Obtenir clé primaire de la table
     *
     * @param entity Entité de la table
     * @return Object clé primaire de la table
     */
    private Object getPrimaryKeyName(Object entity) {
        if (Objects.nonNull(entity)) {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
            }
        }

        return null;
    }

    /**
     * Cette méthode retourne la valeur de l''id de l'entité sur laquelle on effectue l'action
     *
     * @param entity Entité
     * @return Object id de l'entité
     */
    private Object getIdEntityValue(Object entity) {
        return (Objects.isNull(entity))
                ? null
                : entityManager
                .getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(entity);
    }

    /**
     * Cette méthode retourne le nom de la table sur laquelle on effectue l'action
     *
     * @param entity Entité
     * @return String Nom de la table
     */
    private String getTableName(Object entity) {
        if (Objects.nonNull(entity)) {
            Table table = entity.getClass().getAnnotation(Table.class);
            return Objects.nonNull(table) && !table.name().isEmpty() ? table.name() : entity.getClass().getSimpleName();
        }

        return null;
    }
}