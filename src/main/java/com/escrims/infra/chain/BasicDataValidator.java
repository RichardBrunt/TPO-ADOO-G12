package com.escrims.infra.chain;package com.escrims.infra.chain;package com.escrims.infra.chain;



import com.escrims.model.domain.model.Postulacion;

import com.escrims.model.domain.model.Scrim;

import com.escrims.model.domain.chain.PostulacionValidator;import com.escrims.model.domain.model.Postulacion;import com.escrims.model.domain.model.Postulacion;



public class BasicDataValidator extends PostulacionValidator {import com.escrims.model.domain.model.Scrim;import com.escrims.model.domain.chain.PostulacionValidator;

    

    private String mensajeError = "";import com.escrims.model.domain.chain.PostulacionValidator;



    @Override/**

    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {

        boolean isValid = postulacion.getId() != null public class BasicDataValidator extends PostulacionValidator { * Validador concreto - Verifica que la postulacion tenga datos basicos

                       && postulacion.getUsuario() != null

                       && postulacion.getScrim() != null;     */

        

        if (!isValid) {    private String mensajeError = "";public class BasicDataValidator extends PostulacionValidator {

            mensajeError = "Faltan datos basicos en la postulacion";

        }    

        

        return isValid;    @Override    @Override

    }

    protected boolean validarInterno(Postulacion postulacion, Scrim scrim) {    protected boolean doValidate(Postulacion postulacion) {

    @Override

    public String getMensajeError() {        boolean isValid = postulacion.getId() != null         boolean isValid = postulacion.getId() != null 

        return mensajeError;

    }                       && postulacion.getUsuario() != null                       && postulacion.getUsuarioId() != null

}

                       && postulacion.getScrim() != null;                       && postulacion.getScrimId() != null;

                

        if (!isValid) {        if (!isValid) {

            mensajeError = "Faltan datos basicos en la postulacion";            System.out.println("   X " + getValidatorName() + " - RECHAZADA: Faltan datos basicos");

        }        } else {

                    System.out.println("   Ã¢Å“â€œ " + getValidatorName() + " - OK");

        return isValid;        }

    }        

        return isValid;

    @Override    }

    public String getMensajeError() {    

        return mensajeError;    @Override

    }    protected String getValidatorName() {

}        return "Validacion de datos basicos";

    }
}
