package com.escrims.infra.chain;package com.escrims.infra.chain;



import com.escrims.model.domain.model.Postulacion;import com.escrims.model.domain.model.Postulacion;

import com.escrims.model.domain.model.Scrim;import com.escrims.model.domain.chain.PostulacionValidator;

import com.escrims.model.domain.model.Estadistica;

import com.escrims.model.domain.chain.PostulacionValidator;/**

 * Validador concreto - Verifica reglas de negocio complejas

public class BusinessRulesValidator extends PostulacionValidator { * (simulacion: validacion final de integridad)

     */

    private String mensajeError = "";public class BusinessRulesValidator extends PostulacionValidator {

    

    @Override    @Override

    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {    protected boolean doValidate(Postulacion postulacion) {

        // Validar que el scrim acepta postulaciones        // Simulacion de reglas de negocio:

        if (!scrim.getEstado().puedeAceptarPostulaciones()) {        // - El usuario y el scrim deben ser diferentes (no puede postularse a su propio scrim)

            mensajeError = "El scrim no acepta postulaciones en su estado actual";        // - En un caso real, verificaria que el usuario no este baneado, etc.

            return false;        

        }        boolean usuarioValido = postulacion.getUsuarioId() != null;

                boolean scrimValido = postulacion.getScrimId() != null;

        // Validar MMR        boolean noEsMismoId = !postulacion.getUsuarioId().equals(postulacion.getScrimId());

        Estadistica stats = postulacion.getUsuario().getEstadisticaPorJuego(scrim.getJuego());        

        if (stats != null) {        boolean isValid = usuarioValido && scrimValido && noEsMismoId;

            int mmr = stats.getMmr();        

            if (mmr < scrim.getMmrMinimo() || mmr > scrim.getMmrMaximo()) {        if (!isValid) {

                mensajeError = "El MMR del usuario no está en el rango permitido";            System.out.println("   X " + getValidatorName() + " - RECHAZADA: Viola reglas de negocio");

                return false;        } else {

            }            System.out.println("   Ã¢Å“â€œ " + getValidatorName() + " - OK");

        }        }

                

        // Validar lugares disponibles        return isValid;

        if (scrim.getLugaresDisponibles() <= 0) {    }

            mensajeError = "No hay lugares disponibles en este scrim";    

            return false;    @Override

        }    protected String getValidatorName() {

                return "Validacion de reglas de negocio";

        return true;    }

    }}


    @Override
    public String getMensajeError() {
        return mensajeError;
    }
}
