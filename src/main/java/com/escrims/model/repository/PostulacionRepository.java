package com.escrims.model.repository;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface PostulacionRepository {
    Postulacion guardar(Postulacion postulacion);
    Optional<Postulacion> buscarPorId(Long id);
    List<Postulacion> buscarTodas();
    List<Postulacion> buscarPorScrim(Scrim scrim);
    List<Postulacion> buscarPorUsuario(Usuario usuario);
    void eliminar(Long id);
}
