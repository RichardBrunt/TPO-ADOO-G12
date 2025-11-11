package com.escrims.infra.chain;package com.escrims.infra.chain;



import com.escrims.model.domain.model.Postulacion;import com.escrims.model.domain.model.Postulacion;

import com.escrims.model.domain.model.Scrim;import com.escrims.model.domain.model.Rol;

import com.escrims.model.domain.chain.PostulacionValidator;import com.escrims.model.domain.chain.PostulacionValidator;



public class RolValidator extends PostulacionValidator {/**

     * Validador concreto - Verifica que el rol sea valido y no sea nulo

    private String mensajeError = ""; */

public class RolValidator extends PostulacionValidator {

    @Override    

    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {    @Override

        boolean isValid = postulacion.getRolSolicitado() != null;    protected boolean doValidate(Postulacion postulacion) {

                Rol rol = postulacion.getRolDeseado();

        if (!isValid) {        boolean isValid = rol != null;

            mensajeError = "El rol solicitado no puede ser nulo";        

        }        if (!isValid) {

                    System.out.println("   X " + getValidatorName() + " - RECHAZADA: Rol no especificado");

        return isValid;        } else {

    }            System.out.println("   Ã¢Å“â€œ " + getValidatorName() + " - OK (Rol: " + rol + ")");

        }

    @Override        

    public String getMensajeError() {        return isValid;

        return mensajeError;    }

    }    

}    @Override

    protected String getValidatorName() {
        return "Validacion de rol";
    }
}
