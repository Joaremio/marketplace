package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dao.PedidoDAO;
import br.ufrn.imd.marketplace.dao.PedidoProdutoDAO;
import br.ufrn.imd.marketplace.dao.ProdutoDAO;
import br.ufrn.imd.marketplace.dto.ItemPedidoDTO;
import br.ufrn.imd.marketplace.dto.PedidoComItensDTO;
import br.ufrn.imd.marketplace.dto.PedidoProdutoVendedorDTO;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.Pedido;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import br.ufrn.imd.marketplace.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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


    public Pedido criarPedido(Pedido pedido) {
        Connection conn = null;
        try {
            conn = db_connection.getConnection();
            conn.setAutoCommit(false);

            double valorTotalSeguro = 0.0;
            for (PedidoProduto item : pedido.getItens()) {
                ProdutoImagemDTO produtoDoBanco = produtoDAO.buscarProdutoPorId(item.getProdutoId());
                if (produtoDoBanco == null) {
                    throw new RuntimeException("Produto com ID " + item.getProdutoId() + " não encontrado ou inativo.");
                }
                item.setPrecoUnidade(produtoDoBanco.getPreco());
                valorTotalSeguro += item.getPrecoUnidade() * item.getQuantidade();
            }
            pedido.setValorTotal(valorTotalSeguro);

            pedido.setEfetivacao(null);

            Pedido pedidoSalvo = pedidoDAO.criarPedido(pedido, conn);

            for (PedidoProduto item : pedido.getItens()) {
                item.setPedidoId(pedidoSalvo.getId());
                pedidoProdutoDAO.adicionarItemAoPedido(item, conn);
            }

            conn.commit();

            pedidoSalvo.setItens(pedido.getItens());
            return pedidoSalvo;

        } catch (SQLException | RuntimeException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Erro ao criar pedido: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }


    public void excluirPedido(int pedidoId) {
        try {
            pedidoProdutoDAO.excluirItensPorPedidoId(pedidoId);
            pedidoDAO.excluirPedido(pedidoId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pedido buscarPedidoPorId(int pedidoId) {
        try {
            Pedido pedido = pedidoDAO.buscarPedido(pedidoId);
            if (pedido == null) {
                return null;
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
            List<Pedido> pedidos = pedidoDAO.buscarPedidosPorComprador(compradorId);
            if (pedidos.isEmpty()) {
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

    public List<PedidoComItensDTO> buscarPedidosDTOPorComprador(int compradorId) {
        try {
            List<Pedido> pedidos = pedidoDAO.buscarPedidosPorComprador(compradorId);
            if (pedidos.isEmpty()) return Collections.emptyList();

            List<PedidoComItensDTO> pedidosDTO = new ArrayList<>();

            for (Pedido pedido : pedidos) {
                List<PedidoProduto> itens = pedidoProdutoDAO.listarItensParaMultiplosPedidos(List.of(pedido.getId()));

                List<ItemPedidoDTO> itensDTO = new ArrayList<>();
                for (PedidoProduto item : itens) {
                    ItemPedidoDTO itemDTO = new ItemPedidoDTO(
                            item.getProdutoId(),
                            item.getNome(),
                            item.getImageUrl(),
                            item.getQuantidade(),
                            item.getPrecoUnidade(),
                            item.getVendedorNome()
                    );
                    itemDTO.setPedidoId(pedido.getId());
                    itensDTO.add(itemDTO);
                }

                PedidoComItensDTO dto = new PedidoComItensDTO();
                dto.setId(pedido.getId());
                dto.setDataPedido(pedido.getDataPedido().toString());
                dto.setValorTotal(pedido.getValorTotal());
                dto.setStatus(pedido.getStatusPedido().name());
                dto.setItens(itensDTO);


                pedidosDTO.add(dto);
            }

            return pedidosDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}