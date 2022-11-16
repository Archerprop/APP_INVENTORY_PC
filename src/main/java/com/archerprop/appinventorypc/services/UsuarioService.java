package com.archerprop.appinventorypc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder.In;

import lombok.extern.log4j.Log4j2;
import com.archerprop.appinventorypc.entidad.Articulos;
import com.archerprop.appinventorypc.entidad.Usuarios;
import com.archerprop.appinventorypc.repositorios.ArticuloRepositorio;
import com.archerprop.appinventorypc.repositorios.UsuarioRepositorio;

/**
 *
 * @author Aschente
 */
@Service
@Log4j2
public class UsuarioService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public boolean crearUsuario(Usuarios usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuario.getNombre() == null || usuario.getApellido() == null || usuario.getCorreo() == null
                || usuario.getClave() == null || usuario.getCedula() <= 0) {
            return false;
        }
        try {
            if (!usuarioRepositorio.existsByCedula(usuario.getCedula())) {
                usuarioRepositorio.save(usuario);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("Error al crear el usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean autenticar(Usuarios usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuario.getCorreo() == null || usuario.getClave() == null) {
            return false;
        }
        try {
            Usuarios usuarioEncontrado = usuarioRepositorio.findByCorreo(usuario.getCorreo());
            if (usuarioEncontrado.getClave().equals(usuario.getClave())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Error al autenticar el usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean autenticarX(Usuarios usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuario.getCorreo() == null || usuario.getClave() == null) {
            return false;
        }
        String sql = "SELECT * FROM \"usuarios\" WHERE correo=\'" + usuario.getCorreo() + "\' AND clave=\'"
                + usuario.getClave() + "\'";
        Query query = entityManager.createQuery(sql);
        query.setParameter("correo", usuario.getCorreo());
        query.setParameter("clave", usuario.getClave());
        try {
            usuario = (Usuarios) query.getSingleResult();
            if (usuario != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Error al autenticar el usuario: " + e.getMessage());
            return false;
        }
    }
}