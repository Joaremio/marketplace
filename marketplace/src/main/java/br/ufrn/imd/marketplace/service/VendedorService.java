package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.AdministradorDAO;
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

    @Autowired
    private AdministradorDAO administradorDAO;


    public void solicitarVendedor(int usuarioId) {
        try {
            if (administradorDAO.ehAdministrador(usuarioId)) {
                throw new RuntimeException("Este usuário é um administrador e não pode se tornar vendedor.");
            }

            Usuario usuario = usuarioDAO.buscarUsuarioById(usuarioId);
            if (usuario == null) {
                throw new RuntimeException("Usuário com ID " + usuarioId + " não encontrado.");
            }

            vendedorDAO.inserirVendedor(usuarioId);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao processar solicitação de vendedor", e);
        }
    }

    public List<Vendedor> listarVendedores() {
        try{
            return vendedorDAO.getVendedores();
        }catch(SQLException e){
            throw new RuntimeException("Erro ao listar vendedores", e);
        }
    }

    public Vendedor buscarVendedorPorId(int id) {
        try{
            Vendedor vendedor = vendedorDAO.getVendedorById(id);
            if (vendedor == null) {
                throw new RuntimeException("Vendedor com ID " + id + " não encontrado.");
            }
            return vendedor;
        }catch(SQLException e){
            throw new RuntimeException("Erro ao buscar vendedor", e);
        }

    }

    public void excluirVendedor(int id) {
        try{
            vendedorDAO.excluirVendedor(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
