package uv.lis.modelo.dao.impl;

import uv.lis.modelo.conexion.ConexionBD;
import uv.lis.modelo.dao.contratos.ICatalogoDAO;
import uv.lis.modelo.dominio.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO implements ICatalogoDAO {

    private Connection getConn() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public List<Rol> obtenerRoles() throws Exception {
        List<Rol> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM Rol"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Rol(rs.getInt("idRol"), rs.getString("nombreRol")));
            }
        }
        return lista;
    }

    @Override
    public List<MetodoPago> obtenerMetodosPago() throws Exception {
        List<MetodoPago> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM MetodoPago"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new MetodoPago(rs.getInt("idMetodo"), rs.getString("metodo")));
            }
        }
        return lista;
    }

    @Override
    public List<TipoPedido> obtenerTiposPedido() throws Exception {
        List<TipoPedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM TipoPedido"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoPedido(rs.getInt("idTipoPedido"), rs.getString("nombreTipo")));
            }
        }
        return lista;
    }

    @Override
    public List<TipoEstatus> obtenerTiposEstatus() throws Exception {
        List<TipoEstatus> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM TipoEstatus"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoEstatus(rs.getInt("idTipoEstatus"), rs.getString("nombreEstatus")));
            }
        }
        return lista;
    }
}
