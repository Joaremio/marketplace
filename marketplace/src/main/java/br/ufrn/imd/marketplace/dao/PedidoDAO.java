package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.StatusPedido;
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

    // Em PedidoDAO.java - VERSÃO CORRIGIDA
    public Pedido criarPedido(Pedido pedido, Connection conn) throws SQLException {
        String sql = "INSERT INTO pedido (comprador_id, data_pedido, previsao_entrega, efetivacao, total, pagamento_forma, status_pedido) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getCompradorId());
            stmt.setObject(2, pedido.getDataPedido());
            stmt.setObject(3, pedido.getPrevisaoEntrega());

            if (pedido.getEfetivacao() != null) {
                stmt.setObject(4, pedido.getEfetivacao());
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setDouble(5, pedido.getValorTotal());
            stmt.setString(6, pedido.getPagamentoForma());
            stmt.setString(7, "PENDENTE"); // Usando a string diretamente ou StatusPedido.PENDENTE.name()

            // 1. Executar o INSERT
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("A criação do pedido falhou, nenhuma linha afetada.");
            }

            // 2. Recuperar o ID gerado pelo banco de dados
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // 3. Definir o ID no objeto 'pedido'
                    pedido.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("A criação do pedido falhou, nenhum ID foi obtido.");
                }
            }
        }

        // 4. Retornar o objeto 'pedido' agora com o ID correto
        return pedido;
    }


    public Pedido criarPedido(Pedido pedido) throws SQLException {
        try (Connection conn = db_connection.getConnection()) {
            return criarPedido(pedido, conn);
        }
    }

    public void excluirPedido(int pedidoId) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        }
    }

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
                pedido.setStatusPedido(StatusPedido.valueOf(rs.getString("status_pedido")));
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
                pedido.setStatusPedido(StatusPedido.valueOf(rsPedidos.getString("status_pedido")));
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

    public void atualizarStatusPedido(int pedidoId, String novoStatus) throws SQLException {
        String sql = "UPDATE pedido SET status_pedido = ? WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            StatusPedido statusEnum = StatusPedido.valueOf(novoStatus);
            stmt.setString(1, statusEnum.name());

            stmt.setInt(2, pedidoId);
            stmt.executeUpdate();
        }
    }


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
                "AND p.status_pedido IN ('PENDENTE', 'EM_ANDAMENTO', 'ENVIADO', 'CANCELADO')";

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