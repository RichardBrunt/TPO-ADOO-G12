package com.escrims.infra.chain;package com.escrims.infra.chain;



import com.escrims.model.domain.model.Postulacion;import com.escrims.model.domain.model.Postulacion;

import com.escrims.model.domain.model.Scrim;import com.escrims.model.domain.chain.PostulacionValidator;

import com.escrims.model.domain.chain.PostulacionValidator;

/**

public class DuplicateValidator extends PostulacionValidator { * Validador concreto - Verifica que la postulacion no este duplicada

     * (simulacion: verifica que no este ya aceptada o rechazada)

    private String mensajeError = ""; */

public class DuplicateValidator extends PostulacionValidator {

    @Override    

    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {    @Override

        boolean yaPostulado = scrim.getPostulaciones().stream()    protected boolean doValidate(Postulacion postulacion) {

            .anyMatch(p -> p.getUsuario().equals(postulacion.getUsuario())         // Verificar que la postulacion este en estado PENDIENTE

                        && !p.equals(postulacion));        // Si ya fue aceptada o rechazada, es considerada duplicada

                boolean isValid = postulacion.getEstado() == Postulacion.Estado.PENDIENTE;

        if (yaPostulado) {        

            mensajeError = "El usuario ya se ha postulado a este scrim";        if (!isValid) {

        }            System.out.println("   X " + getValidatorName() + " - RECHAZADA: Ya procesada (Estado: " + postulacion.getEstado() + ")");

                } else {

        return !yaPostulado;            System.out.println("   Ã¢Å“â€œ " + getValidatorName() + " - OK");

    }        }

        

    @Override        return isValid;

    public String getMensajeError() {    }

        return mensajeError;    

    }    @Override

}    protected String getValidatorName() {

        return "Validacion de duplicados";
    }
}
