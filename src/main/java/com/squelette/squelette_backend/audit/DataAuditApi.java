package com.squelette.squelette_backend.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataAuditApi {

    @JsonProperty(value = "url")
    private String url;

    @JsonProperty(value = "statut")
    private int status;

    @JsonProperty(value = "methode_http")
    private String httpMethod;
}
