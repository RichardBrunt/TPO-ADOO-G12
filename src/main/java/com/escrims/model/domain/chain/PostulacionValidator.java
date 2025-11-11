package com.escrims.model.domain.chain;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;

public abstract class PostulacionValidator {
    protected PostulacionValidator siguiente;

    public void setSiguiente(PostulacionValidator siguiente) {
        this.siguiente = siguiente;
    }

    public boolean validar(Postulacion postulacion, Scrim scrim) {
        boolean resultado = validarInterno(postulacion, scrim);
        if (resultado && siguiente != null) {
            return siguiente.validar(postulacion, scrim);
        }
        return resultado;
    }

    protected abstract boolean validarInterno(Postulacion postulacion, Scrim scrim);
    public abstract String getMensajeError();
}
