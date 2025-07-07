package br.ufrn.imd.marketplace.dao;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.service.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PedidoDAO {

    @Autowired
    private DB_Connection db_connection;

    @Autowired
    private PedidoProdutoDAO pedidoProdutoDAO;

    public Pedido criarPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (comprador_id, data_pedido, previsao_entrega, efetivacao, total, pagamento_forma) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getCompradorId());
            stmt.setObject(2, pedido.getDataPedido());
            stmt.setObject(3, pedido.getPrevisaoEntrega());
            stmt.setString(4, pedido.getEfetivacao());
            stmt.setDouble(5, pedido.getValorTotal());
            stmt.setString(6, pedido.getPagamentoForma());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                pedido.setId(generatedKeys.getInt(1));
            }

            return pedido;
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

    public List<Pedido> buscarPedidosPorComprador(int compradorId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE comprador_id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, compradorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setCompradorId(rs.getInt("comprador_id"));
                pedido.setDataPedido(rs.getObject("data_pedido", LocalDate.class));
                pedido.setStatusPedido(rs.getString("status_pedido"));
                pedido.setEfetivacao(rs.getString("efetivacao"));
                pedido.setPrevisaoEntrega(rs.getObject("previsao_entrega", LocalDate.class));
                pedido.setValorTotal(rs.getDouble("total"));
                pedido.setPagamentoForma(rs.getString("pagamento_forma"));
                pedido.setItens(pedidoProdutoDAO.ListarItensDoPedido(pedido.getId()));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    public void atualizarStatusPedido(int pedidoId, String novoStatus) throws SQLException {
        String sql = "UPDATE pedido SET status_pedido = ? WHERE id = ?";
        try (Connection conn = db_connection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoStatus);
            stmt.setInt(2, pedidoId);
            stmt.executeUpdate();
        }
    }
}

