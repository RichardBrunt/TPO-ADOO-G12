package com.escrims.infra.chain;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.chain.PostulacionValidator;

public class BasicDataValidator extends PostulacionValidator {
    private String mensajeError = "";

    @Override
    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {
        boolean isValid = postulacion.getId() != null
                       && postulacion.getUsuario() != null
                       && postulacion.getScrim() != null;
        if (!isValid) {
            mensajeError = "Faltan datos basicos en la postulacion";
        }
        return isValid;
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }
}
