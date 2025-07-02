package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.PedidoProdutoDAO;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class PedidoProdutoService {

    @Autowired
    private PedidoProdutoDAO pedidoProdutoDAO;

    public void AdicionarItemAoPedido(PedidoProduto item) {
        try{
            pedidoProdutoDAO.adicionarItemAoPedido(item);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ExcluirItemAoPedido(int pedidoId, int itemId) {
        try{
            pedidoProdutoDAO.ExcluirItemAoPedido(pedidoId, itemId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
