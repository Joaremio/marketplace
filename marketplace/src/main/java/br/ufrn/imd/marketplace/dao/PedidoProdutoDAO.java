package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
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

    /**
     * Versão sobrecarregada que aceita uma conexão externa para transações.
     * @param item O PedidoProduto a ser salvo.
     * @param conn A conexão de banco de dados já existente e transacional.
     */
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

    /**
     * Método original mantido para uso fora de transações complexas.
     */
    public void adicionarItemAoPedido(PedidoProduto item) throws SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            adicionarItemAoPedido(item, conn);
        }
    }

    /**
     * Busca os itens de um único pedido, incluindo o nome do produto.
     * @param pedidoId O ID do pedido.
     * @return Uma lista de PedidoProduto com todos os dados, incluindo o nome.
     */
    public List<PedidoProduto> ListarItensDoPedido(int pedidoId) throws SQLException {
        List<PedidoProduto> itens = new ArrayList<>();

        // 🚨 Atualize sua query para também trazer o nome do vendedor
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
                item.setNome(rs.getString("nome_produto")); // Nome do produto
                item.setVendedorNome(rs.getString("nome_vendedor")); // 💡 Nome do vendedor
                itens.add(item);
            }
        }
        return itens;
    }


    /**
     * Essencial para corrigir o problema de performance N+1.
     * Busca todos os itens de múltiplos pedidos em uma única consulta.
     * @param pedidoIds Uma lista de IDs de pedidos.
     * @return Uma lista com todos os PedidoProduto encontrados, incluindo o nome.
     */
    public List<PedidoProduto> listarItensParaMultiplosPedidos(List<Integer> pedidoIds) throws SQLException {
        if (pedidoIds == null || pedidoIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<PedidoProduto> itens = new ArrayList<>();
        String placeholders = String.join(",", Collections.nCopies(pedidoIds.size(), "?"));

        String sql = "SELECT pp.*, p.nome " +
                "FROM pedido_produto pp " +
                "JOIN produto p ON pp.PRODUTO_id = p.id " +
                "WHERE pp.PEDIDO_id IN (" + placeholders + ")";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < pedidoIds.size(); i++) {
                stmt.setInt(i + 1, pedidoIds.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PedidoProduto item = new PedidoProduto();
                item.setPedidoId(rs.getInt("PEDIDO_id"));
                item.setProdutoId(rs.getInt("PRODUTO_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnidade(rs.getDouble("preco_unitario"));
                item.setNome(rs.getString("nome")); // Preenchendo o nome do produto
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

    /**
     * Usado para limpar os itens de um pedido, por exemplo, ao excluir o pedido pai.
     * @param pedidoId O ID do pedido cujos itens serão excluídos.
     */
    public void excluirItensPorPedidoId(int pedidoId) throws SQLException {
        String sql = "DELETE FROM pedido_produto WHERE PEDIDO_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        }
    }
}