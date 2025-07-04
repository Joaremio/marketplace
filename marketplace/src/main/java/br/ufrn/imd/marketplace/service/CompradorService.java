package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dao.CompradorDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Comprador;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class CompradorService {

    @Autowired
    private CompradorDAO compradorDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private DB_Connection dbConnection;

    public void inserirComprador(int usuarioId) {
        try {
            Connection conn = dbConnection.getConnection();
            Usuario usuario = usuarioDAO.buscarUsuarioById(usuarioId);
            if (usuario == null) {
                throw new RuntimeException("Usuário com ID " + usuarioId + " não encontrado.");
            }
            compradorDAO.inserirComprador(conn,usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário no banco de dados", e);
        }
    }

    public List<Comprador> listarCompradores() {
        try{return compradorDAO.getCompradores();} catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void deletarComprador(int usuarioId) {
        try{compradorDAO.removerComprador(usuarioId);} catch (SQLException e) {throw new RuntimeException(e);}
    }
}
