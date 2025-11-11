package com.escrims.infra.chain;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.chain.PostulacionValidator;

public class RolValidator extends PostulacionValidator {
    private String mensajeError = "";

    @Override
    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {
        boolean isValid = postulacion.getRolSolicitado() != null;
        if (!isValid) {
            mensajeError = "El rol solicitado no puede ser nulo";
        }
        return isValid;
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }
}
