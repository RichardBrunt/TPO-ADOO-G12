package com.escrims.model.repository;

import com.escrims.model.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorUsername(String username);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    void eliminar(Long id);
    boolean existePorUsername(String username);
    boolean existePorEmail(String email);
}
