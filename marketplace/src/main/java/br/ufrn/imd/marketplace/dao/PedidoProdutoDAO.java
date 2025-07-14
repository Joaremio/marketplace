package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.AvaliacaoRequest;
import br.ufrn.imd.marketplace.dto.ItemPedidoDTO;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class PedidoProdutoDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void adicionarItemAoPedido(PedidoProduto item, Connection conn) throws SQLException {
        String sql = "INSERT INTO pedido_produto (PEDIDO_id, PRODUTO_id, quantidade, preco_unitario) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getPedidoId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnidade());
            stmt.executeUpdate();
        }
    }

    public void adicionarItemAoPedido(PedidoProduto item) throws SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            adicionarItemAoPedido(item, conn);
        }
    }

    public List<PedidoProduto> ListarItensDoPedido(int pedidoId) throws SQLException {
        List<PedidoProduto> itens = new ArrayList<>();

        String sql = "SELECT pp.*, p.nome AS nome_produto, u.nome AS nome_vendedor " +
                "FROM pedido_produto pp " +
                "JOIN produto p ON pp.PRODUTO_id = p.id " +
                "JOIN usuario u ON p.vendedor_id = u.id " +
                "WHERE pp.PEDIDO_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PedidoProduto item = new PedidoProduto();
                item.setPedidoId(rs.getInt("PEDIDO_id"));
                item.setProdutoId(rs.getInt("PRODUTO_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnidade(rs.getDouble("preco_unitario"));
                item.setNome(rs.getString("nome_produto"));
                item.setVendedorNome(rs.getString("nome_vendedor"));
                itens.add(item);
            }
        }
        return itens;
    }

    public List<PedidoProduto> listarItensParaMultiplosPedidos(List<Integer> pedidoIds) throws SQLException {
        if (pedidoIds == null || pedidoIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<PedidoProduto> itens = new ArrayList<>();
        String placeholders = String.join(",", Collections.nCopies(pedidoIds.size(), "?"));

        String sql = "SELECT " +
                "    pp.quantidade, " +
                "    pp.preco_unitario, " +
                "    pp.produto_id, " +
                "    pp.pedido_id, " +
                "    p.nome AS produto_nome, " +
                "    u.nome AS vendedor_nome, " +
                "    i.imagem AS imagem_url " +
                "FROM " +
                "    pedido_produto pp " +
                "JOIN produto p ON pp.produto_id = p.id " +
                "JOIN vendedor v ON p.vendedor_id = v.usuario_id " +
                "JOIN usuario u ON v.usuario_id = u.id " +
                "LEFT JOIN imagem i ON i.PRODUTO_ID = p.id " +
                "WHERE pp.pedido_id IN (" + placeholders + ")";



        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < pedidoIds.size(); i++) {
                stmt.setInt(i + 1, pedidoIds.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PedidoProduto item = new PedidoProduto();
                item.setPedidoId(rs.getInt("pedido_id"));
                item.setProdutoId(rs.getInt("produto_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnidade(rs.getDouble("preco_unitario"));
                item.setNome(rs.getString("produto_nome"));
                item.setVendedorNome(rs.getString("vendedor_nome"));
                String url = rs.getString("imagem_url");
                item.setImageUrl(url != null ? url : "https://via.placeholder.com/150");
                itens.add(item);
            }
        }
        return itens;
    }

    public void ExcluirItemAoPedido(int pedidoId, int itemId) throws SQLException {
        String sql = "DELETE from pedido_produto WHERE PEDIDO_id = ? AND PRODUTO_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }

    public void excluirItensPorPedidoId(int pedidoId) throws SQLException {
        String sql = "DELETE FROM pedido_produto WHERE PEDIDO_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        }
    }

    public void registrarAvaliacao(Integer pedidoId, Integer produtoId, String avaliacao) throws SQLException {

        String sql = "UPDATE pedido_produto SET avaliacao = ?  WHERE PEDIDO_id = ? AND PRODUTO_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, avaliacao);
            stmt.setInt(2, pedidoId);
            stmt.setInt(3, produtoId);

            stmt.executeUpdate();
        }
    }

    public List<AvaliacaoRequest> listarAvaliacoesPorProduto(int produtoId) throws SQLException {
        List<AvaliacaoRequest> avaliacoes = new ArrayList<>();

        String sql = "SELECT " +
                "    pp.avaliacao, " +
                "    u.nome AS nome_comprador " +
                "FROM " +
                "    pedido_produto pp " +
                "JOIN " +
                "    pedido p ON pp.PEDIDO_id = p.id " +
                "JOIN " +
                "    usuario u ON p.comprador_id = u.id " +
                "WHERE " +
                "    pp.PRODUTO_id = ? AND pp.avaliacao IS NOT NULL ";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String avaliacao = rs.getString("avaliacao");
                String nomeComprador = rs.getString("nome_comprador");


                avaliacoes.add(new AvaliacaoRequest(avaliacao, nomeComprador));
            }
        }
        return avaliacoes;
    }
}