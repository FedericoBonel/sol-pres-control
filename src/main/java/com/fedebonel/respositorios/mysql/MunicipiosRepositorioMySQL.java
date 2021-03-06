package com.fedebonel.respositorios.mysql;

import com.fedebonel.modelo.accesodatos.ConexionDB;
import com.fedebonel.modelo.municipio.Municipio;
import com.fedebonel.respositorios.MunicipiosRepositorio;
import com.fedebonel.respositorios.UsuariosRepositorio;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Repositorio de municipios en MySQL
 */
public class MunicipiosRepositorioMySQL implements MunicipiosRepositorio {

    private final UsuariosRepositorio usuariosRepositorio;

    public MunicipiosRepositorioMySQL(UsuariosRepositorio usuariosRepositorio) {
        this.usuariosRepositorio = usuariosRepositorio;
    }


    @Override
    public void guardar(Municipio entidad) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("insert into municipio values (?, ?, ?, NULL, NULL)")) {
            stmt.setString(1, entidad.getId());
            stmt.setString(2, entidad.getNombre());
            stmt.setInt(3, entidad.getCategoria());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Municipio> leerTodo() throws SQLException {
        List<Municipio> result = new LinkedList<>();
        Municipio currMunicipio;
        String fiscalId, cuentadanteId;
        Connection conn = ConexionDB.getConnection();
        ResultSet rs;
        try (Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery("select * from municipio");
            while (rs.next()) {
                currMunicipio = new Municipio(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3));
                fiscalId = rs.getString(4);
                cuentadanteId = rs.getString(5);
                if (fiscalId != null)
                    currMunicipio.tomaNuevoSupervisorFiscal(usuariosRepositorio.leerPorId(fiscalId));
                if (cuentadanteId != null)
                    currMunicipio.tomaNuevoRepresentante(usuariosRepositorio.leerPorId(cuentadanteId), result);
                result.add(currMunicipio);
            }
            return result;
        }
    }

    @Override
    public Municipio leerPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        ResultSet rs;
        try (PreparedStatement stmt = conn.prepareStatement("select * from municipio where identificador=?")) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) return null;
            Municipio municipio = new Municipio(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3));
            String fiscalId = rs.getString(4);
            if (fiscalId != null)
                municipio.tomaNuevoSupervisorFiscal(usuariosRepositorio.leerPorId(fiscalId));
            String cuentadanteId = rs.getString(5);
            if (cuentadanteId != null)
                municipio.tomaNuevoRepresentante(usuariosRepositorio.leerPorId(cuentadanteId), leerTodo());
            return municipio;
        }
    }

    @Override
    public void eliminarPorId(String id) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("delete from municipio where identificador = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void actualizarPorId(String id, String campo, String valor) throws SQLException {
        Connection conn = ConexionDB.getConnection();
        PreparedStatement stmt = null;
        try {
            if (valor.equals("NULL")) {
                stmt = conn.prepareStatement("update municipio set " + campo + " = NULL where identificador = ?");
                stmt.setString(1, id);
            } else {
                stmt = conn.prepareStatement("update municipio set " + campo + " = ? where identificador = ?");
                stmt.setString(1, valor);
                stmt.setString(2, id);
            }
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
}
