package com.escrims.model.domain.template;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import java.util.List;

public abstract class ScrimMatchmakingTemplate {
    
    public final List<Postulacion> ejecutarMatchmaking(Scrim scrim) {
        validarScrim(scrim);
        List<Postulacion> postulacionesValidas = filtrarPostulaciones(scrim);
        postulacionesValidas = ordenarPostulaciones(postulacionesValidas);
        postulacionesValidas = seleccionarJugadores(scrim, postulacionesValidas);
        notificarResultados(scrim, postulacionesValidas);
        return postulacionesValidas;
    }

    protected abstract void validarScrim(Scrim scrim);
    protected abstract List<Postulacion> filtrarPostulaciones(Scrim scrim);
    protected abstract List<Postulacion> ordenarPostulaciones(List<Postulacion> postulaciones);
    protected abstract List<Postulacion> seleccionarJugadores(Scrim scrim, List<Postulacion> postulaciones);
    protected abstract void notificarResultados(Scrim scrim, List<Postulacion> seleccionadas);
}
