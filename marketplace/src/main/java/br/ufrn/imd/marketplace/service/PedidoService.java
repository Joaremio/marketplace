package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.PedidoDAO;
import br.ufrn.imd.marketplace.dao.PedidoProdutoDAO;
import br.ufrn.imd.marketplace.dao.ProdutoDAO;
import br.ufrn.imd.marketplace.dto.PedidoProdutoVendedorDTO;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import br.ufrn.imd.marketplace.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class PedidoService {

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private ProdutoDAO produtoDAO;



    @Autowired
    private PedidoProdutoDAO pedidoProdutoDAO;

    public Pedido criarPedido(Pedido pedido) {
        try{
            pedido.setValorTotal(calcularTotalPedido(pedido.getItens()));
            return pedidoDAO.criarPedido(pedido);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double calcularTotalPedido(List<PedidoProduto> itens) {
        double total = 0;
        for(PedidoProduto item : itens ) {
            total += item.getPrecoUnidade()*item.getQuantidade();
        }
        return total;
    }

    public void excluirPedido(int pedidoId) {
        try{
            pedidoDAO.excluirPedido(pedidoId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pedido buscarPedidoPorId(int pedidoId) {
        try {
            Pedido pedido = pedidoDAO.buscarPedido(pedidoId);
            if (pedido == null) {
                throw new RuntimeException("Pedido não encontrado");
            }
            List<PedidoProduto> itens = pedidoProdutoDAO.ListarItensDoPedido(pedidoId);
            pedido.setItens(itens);

            return pedido;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void atualizarStatusPedido(int pedidoId, String status) {
        try{
            pedidoDAO.atualizarStatusPedido(pedidoId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> buscarPedidosPorComprador(int compradorId){
        try{
            List<Pedido> pedidos = pedidoDAO.buscarPedidosPorComprador(compradorId);
            if(pedidos.isEmpty()){
                throw new RuntimeException("Comprador ainda não possui pedidos");
            }
            return pedidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PedidoProdutoVendedorDTO> buscarPedidosPorVendedor(int vendedorId){
        try{
            List<PedidoProdutoVendedorDTO> pedidos = pedidoDAO.buscarPedidosPendentesPorVendedor(vendedorId);
            return pedidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
