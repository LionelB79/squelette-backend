package com.squelette.squelette_backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class CodeMessage implements Serializable {

    private int code;
    private String message;

    /**
     * Permet de formater la string contenu dans le message sans impacter l'objet initiale.
     * @param args Lister des arguments à formatter.
     * @return Une copie du CodeMessage initiale avec le message formatté.
     */
    public CodeMessage formatMessage(Object... args) {
        return new CodeMessage(
                code,
                String.format(message, args)
        );
    }
}
