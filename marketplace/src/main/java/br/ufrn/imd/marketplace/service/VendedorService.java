package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.dao.VendedorDAO;
import br.ufrn.imd.marketplace.model.Usuario;
import br.ufrn.imd.marketplace.model.Vendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class VendedorService {

    @Autowired
    private VendedorDAO vendedorDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    public void solicitarVendedor(int usuarioId) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioById(usuarioId);
            if (usuario == null) {
                throw new RuntimeException("Usuário com ID " + usuarioId + " não encontrado.");
            }
            vendedorDAO.inserirVendedor(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário no banco de dados", e);
        }
    }

    public List<Vendedor> listarVendedores() {
        return vendedorDAO.getVendedores();
    }

    public Vendedor buscarVendedorPorId(int id) {
        Vendedor vendedor = vendedorDAO.getVendedorById(id);
        if (vendedor == null) {
            throw new RuntimeException("Vendedor com ID " + id + " não encontrado.");
        }
        return vendedor;
    }

    public void excluirVendedor(int id) {
        vendedorDAO.excluirVendedor(id);
    }
}
