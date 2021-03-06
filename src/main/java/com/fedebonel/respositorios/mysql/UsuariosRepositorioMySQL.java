package com.fedebonel.respositorios.mysql;

import com.fedebonel.modelo.accesodatos.ConexionDB;
import com.fedebonel.modelo.usuario.RolUsuario;
import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.respositorios.UsuariosRepositorio;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Repositorio de usuarios en MySQL
 */
public class UsuariosRepositorioMySQL implements UsuariosRepositorio {
    @Override
    public void guardar(Usuario entidad) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("insert into usuario values (?, ?, ?, ?)")) {
            stmt.setString(1, entidad.getId());
            stmt.setString(2, entidad.getNombre());
            stmt.setString(3, entidad.getClave());
            stmt.setString(4, entidad.rolUsuario.getNombreRol());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Usuario> leerTodo() throws SQLException {
        List<Usuario> result = new LinkedList<>();
        Usuario currUsuario;
        Connection conn = ConexionDB.getConnection();
        ResultSet rs;
        try (Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery("select * from usuario");
            while (rs.next()) {
                currUsuario = new Usuario(
                        rs.getString(2),
                        rs.getString(1),
                        rs.getString(3),
                        new RolUsuario(rs.getString(4)));
                result.add(currUsuario);
            }
            return result;
        }
    }

    @Override
    public Usuario leerPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        ResultSet rs;
        try (PreparedStatement stmt = conn.prepareStatement("select * from usuario where identificador=?")) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) return null;
            return new Usuario(
                    rs.getString(2),
                    rs.getString(1),
                    rs.getString(3),
                    new RolUsuario(rs.getString(4)));
        }
    }

    @Override
    public void eliminarPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("delete from usuario where identificador = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void actualizarPorId(String id, String campo, String valor) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("update usuario set " + campo + " = ? where identificador = ?")) {
            stmt.setString(1, valor);
            stmt.setString(2, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Usuario searchByName(String name) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        ResultSet rs;
        try (PreparedStatement stmt = conn.prepareStatement("select * from usuario where nombre=?")) {
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (!rs.next()) return null;
            return new Usuario(
                    rs.getString(2),
                    rs.getString(1),
                    rs.getString(3),
                    new RolUsuario(rs.getString(4)));
        }
    }
}
