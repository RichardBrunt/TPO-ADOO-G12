package com.escrims.infra.chain;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.model.Estadistica;
import com.escrims.model.domain.chain.PostulacionValidator;

public class BusinessRulesValidator extends PostulacionValidator {
    private String mensajeError = "";

    @Override
    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {
        if (!scrim.getEstado().puedeAceptarPostulaciones()) {
            mensajeError = "El scrim no acepta postulaciones en su estado actual";
            return false;
        }

        Estadistica stats = postulacion.getUsuario().getEstadisticaPorJuego(scrim.getJuego());
        if (stats != null) {
            int mmr = stats.getMmr();
            if (mmr < scrim.getMmrMinimo() || mmr > scrim.getMmrMaximo()) {
                mensajeError = "El MMR del usuario no esta en el rango permitido";
                return false;
            }
        }

        if (scrim.getLugaresDisponibles() <= 0) {
            mensajeError = "No hay lugares disponibles en este scrim";
            return false;
        }

        return true;
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }
}
