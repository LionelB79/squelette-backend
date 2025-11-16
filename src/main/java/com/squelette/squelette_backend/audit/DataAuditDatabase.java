package com.squelette.squelette_backend.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataAuditDatabase {

    @JsonProperty(value = "id_entite")
    private Object primaryKeyName;

    @JsonProperty(value = "valeur_id_entite")
    private Object valueIdEntity;

    @JsonProperty(value = "nom_table")
    private String tableName;

    @JsonProperty(value = "valeur_avant")
    private JsonNode beforeValue;

    @JsonProperty(value = "valeur_apres")
    private JsonNode afterValue;
}
