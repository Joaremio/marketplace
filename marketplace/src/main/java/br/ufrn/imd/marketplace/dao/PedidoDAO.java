package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.PedidoProdutoVendedorDTO;
import br.ufrn.imd.marketplace.model.Pedido;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PedidoDAO {

    @Autowired
    private DB_Connection db_connection;

    @Autowired
    private PedidoProdutoDAO pedidoProdutoDAO;

    // Dentro da classe PedidoDAO.java

    // MUDANÇA: Método sobrecarregado que aceita uma conexão externa para transações
    public Pedido criarPedido(Pedido pedido, Connection conn) throws SQLException {
        String sql = "INSERT INTO pedido (comprador_id, data_pedido, previsao_entrega, efetivacao, total, pagamento_forma, status_pedido) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getCompradorId());
            stmt.setObject(2, pedido.getDataPedido());
            stmt.setObject(3, pedido.getPrevisaoEntrega());

            // CORREÇÃO: Tratar corretamente o valor para a coluna DATETIME 'efetivacao'
            if (pedido.getEfetivacao() != null) {
                // Este bloco será usado no futuro, quando você for ATUALIZAR um pedido como "entregue"
                stmt.setObject(4, pedido.getEfetivacao());
            } else {
                // Na CRIAÇÃO do pedido, o valor é nulo, então usamos setNull com o tipo SQL correto
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setDouble(5, pedido.getValorTotal());
            stmt.setString(6, pedido.getPagamentoForma());
            stmt.setString(7, pedido.getStatusPedido());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getInt(1));
                }
            }
        }
        return pedido;
    }

    // O método original pode ser mantido para chamadas não transacionais, se necessário
    public Pedido criarPedido(Pedido pedido) throws SQLException {
        try (Connection conn = db_connection.getConnection()) {
            return criarPedido(pedido, conn);
        }
    }

    // MÉTODO ADICIONADO - Estava faltando no arquivo anterior
    public void excluirPedido(int pedidoId) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        }
    }

    // MÉTODO ADICIONADO - Estava faltando no arquivo anterior
    public Pedido buscarPedido(int pedidoId) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setCompradorId(rs.getInt("comprador_id"));
                pedido.setDataPedido(rs.getObject("data_pedido", LocalDate.class));
                pedido.setStatusPedido(rs.getString("status_pedido"));
                pedido.setEfetivacao(rs.getString("efetivacao"));
                pedido.setPrevisaoEntrega(rs.getObject("previsao_entrega", LocalDate.class));
                pedido.setValorTotal(rs.getDouble("total"));
                pedido.setPagamentoForma(rs.getString("pagamento_forma"));
                return pedido;
            } else {
                return null;
            }
        }
    }


    // MUDANÇA: CORREÇÃO DO PROBLEMA N+1 QUERY
    public List<Pedido> buscarPedidosPorComprador(int compradorId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sqlPedidos = "SELECT * FROM pedido WHERE comprador_id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos)) {

            stmtPedidos.setInt(1, compradorId);
            ResultSet rsPedidos = stmtPedidos.executeQuery();

            while (rsPedidos.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rsPedidos.getInt("id"));
                pedido.setCompradorId(rsPedidos.getInt("comprador_id"));
                pedido.setDataPedido(rsPedidos.getObject("data_pedido", LocalDate.class));
                pedido.setStatusPedido(rsPedidos.getString("status_pedido"));
                pedido.setEfetivacao(rsPedidos.getString("efetivacao"));
                pedido.setPrevisaoEntrega(rsPedidos.getObject("previsao_entrega", LocalDate.class));
                pedido.setValorTotal(rsPedidos.getDouble("total"));
                pedido.setPagamentoForma(rsPedidos.getString("pagamento_forma"));
                pedidos.add(pedido);
            }
        }

        if (pedidos.isEmpty()) {
            return pedidos;
        }

        List<Integer> pedidoIds = pedidos.stream().map(Pedido::getId).collect(Collectors.toList());
        List<PedidoProduto> todosOsItens = pedidoProdutoDAO.listarItensParaMultiplosPedidos(pedidoIds);

        Map<Integer, List<PedidoProduto>> itensPorPedidoId = todosOsItens.stream()
                .collect(Collectors.groupingBy(PedidoProduto::getPedidoId));

        for (Pedido pedido : pedidos) {
            pedido.setItens(itensPorPedidoId.getOrDefault(pedido.getId(), new ArrayList<>()));
        }

        return pedidos;
    }

    // MÉTODO ADICIONADO - Estava faltando no arquivo anterior
    public void atualizarStatusPedido(int pedidoId, String novoStatus) throws SQLException {
        String sql = "UPDATE pedido SET status_pedido = ? WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, pedidoId);
            stmt.executeUpdate();
        }
    }

    // MÉTODO ADICIONADO - Estava faltando no arquivo anterior
    public List<PedidoProdutoVendedorDTO> buscarPedidosPendentesPorVendedor(int vendedorId) throws SQLException {
        List<PedidoProdutoVendedorDTO> lista = new ArrayList<>();
        String sql = "SELECT " +
                "  p.id AS pedido_id, " +
                "  p.data_pedido, " +
                "  p.status_pedido, " +
                "  pr.id AS produto_id, " +
                "  pr.nome AS nome_produto, " +
                "  pp.quantidade, " +
                "  pp.preco_unitario " +
                "FROM pedido p " +
                "JOIN pedido_produto pp ON p.id = pp.pedido_id " +
                "JOIN produto pr ON pp.produto_id = pr.id " +
                "WHERE pr.vendedor_id = ? " +
                "AND p.status_pedido IN ('Pendente', 'Em Andamento')";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PedidoProdutoVendedorDTO dto = new PedidoProdutoVendedorDTO(
                        rs.getInt("pedido_id"),
                        rs.getObject("data_pedido", LocalDate.class),
                        rs.getString("status_pedido"),
                        rs.getInt("produto_id"),
                        rs.getString("nome_produto"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                );
                lista.add(dto);
            }
        }
        return lista;
    }
}