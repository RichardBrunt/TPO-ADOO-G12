package com.escrims.infra.chain;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.chain.PostulacionValidator;

public class DuplicateValidator extends PostulacionValidator {
    private String mensajeError = "";

    @Override
    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {
        boolean yaPostulado = scrim.getPostulaciones().stream()
            .anyMatch(p -> p.getUsuario().equals(postulacion.getUsuario())
                        && !p.equals(postulacion));
        if (yaPostulado) {
            mensajeError = "El usuario ya se ha postulado a este scrim";
        }
        return !yaPostulado;
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }
}
