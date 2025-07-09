package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dao.PedidoDAO;
import br.ufrn.imd.marketplace.dao.PedidoProdutoDAO;
import br.ufrn.imd.marketplace.dao.ProdutoDAO;
import br.ufrn.imd.marketplace.dto.PedidoProdutoVendedorDTO;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.Pedido;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import br.ufrn.imd.marketplace.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@Service
public class PedidoService {

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private PedidoProdutoDAO pedidoProdutoDAO;

    @Autowired
    private ProdutoDAO produtoDAO;

    @Autowired
    private DB_Connection db_connection;

    // MUDANÇA: LÓGICA DE TRANSAÇÃO MANUAL
    // Dentro da classe PedidoService.java

    public Pedido criarPedido(Pedido pedido) {
        Connection conn = null;
        try {
            // 1. Obter uma conexão e iniciar a transação manual
            conn = db_connection.getConnection();
            conn.setAutoCommit(false);

            // 2. SEGURANÇA: Calcular total com preços do banco de dados
            double valorTotalSeguro = 0.0;
            for (PedidoProduto item : pedido.getItens()) {
                ProdutoImagemDTO produtoDoBanco = produtoDAO.buscarProdutoPorId(item.getProdutoId());
                if (produtoDoBanco == null) {
                    throw new RuntimeException("Produto com ID " + item.getProdutoId() + " não encontrado ou inativo.");
                }
                // Define o preço unitário seguro no item do pedido
                item.setPrecoUnidade(produtoDoBanco.getPreco());
                valorTotalSeguro += item.getPrecoUnidade() * item.getQuantidade();
            }
            pedido.setValorTotal(valorTotalSeguro);

            // 3. CORREÇÃO: Garantir que a data de efetivação seja NULA na criação do pedido.
            // A efetivação só ocorre quando o pedido é concluído/entregue.
            pedido.setEfetivacao(null);

            // 4. Salvar o pedido principal para gerar o ID, usando a mesma conexão
            Pedido pedidoSalvo = pedidoDAO.criarPedido(pedido, conn);

            // 5. Associar o ID gerado aos itens e salvá-los, usando a mesma conexão
            for (PedidoProduto item : pedido.getItens()) {
                item.setPedidoId(pedidoSalvo.getId());
                pedidoProdutoDAO.adicionarItemAoPedido(item, conn);
            }

            // 6. Se tudo deu certo, comitar (salvar permanentemente) a transação
            conn.commit();

            pedidoSalvo.setItens(pedido.getItens());
            return pedidoSalvo;

        } catch (SQLException | RuntimeException e) {
            // 7. Se algo deu errado, fazer rollback (desfazer todas as operações)
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                // Em um sistema real, isso seria um log de erro crítico
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Erro ao criar pedido: " + e.getMessage(), e);
        } finally {
            // 8. Sempre fechar a conexão e restaurar o auto-commit
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                // Em um sistema real, isso seria um log de erro
                closeEx.printStackTrace();
            }
        }
    }


    public void excluirPedido(int pedidoId) {
        try {
            // Assumindo que excluir um pedido também deve excluir seus itens (CASCADE no BD ou lógica aqui)
            pedidoProdutoDAO.excluirItensPorPedidoId(pedidoId); // Método a ser criado no DAO
            pedidoDAO.excluirPedido(pedidoId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pedido buscarPedidoPorId(int pedidoId) {
        try {
            Pedido pedido = pedidoDAO.buscarPedido(pedidoId);
            if (pedido == null) {
                return null; // Retorna null se não encontrar, o Controller decide o que fazer
            }
            List<PedidoProduto> itens = pedidoProdutoDAO.ListarItensDoPedido(pedidoId);
            pedido.setItens(itens);
            return pedido;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void atualizarStatusPedido(int pedidoId, String status) {
        try {
            pedidoDAO.atualizarStatusPedido(pedidoId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> buscarPedidosPorComprador(int compradorId) {
        try {
            // CORREÇÃO: O método do DAO agora é eficiente e não tem o problema N+1
            List<Pedido> pedidos = pedidoDAO.buscarPedidosPorComprador(compradorId);
            if (pedidos.isEmpty()) {
                // CORREÇÃO: Retorna lista vazia em vez de lançar exceção.
                return Collections.emptyList();
            }
            return pedidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PedidoProdutoVendedorDTO> buscarPedidosPorVendedor(int vendedorId) {
        try {
            return pedidoDAO.buscarPedidosPendentesPorVendedor(vendedorId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}