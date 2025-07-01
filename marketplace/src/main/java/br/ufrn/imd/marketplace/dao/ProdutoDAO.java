package br.ufrn.imd.marketplace.dao;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {

    @Autowired
    private DB_Connection dbConnection;

    public Produto cadastrarProduto(int vendedorId, Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (vendedor_id, nome, preco, descricao, estoque, ativo, categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vendedorId);
            stmt.setString(2, produto.getNome());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getEstoque());
            stmt.setBoolean(6, produto.isAtivo());
            stmt.setString(7, produto.getCategoria());

            stmt.executeUpdate();

            // Recupera o ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }

            produto.setVendedorId(vendedorId);

            return produto;
        }
    }


    public List<Produto> buscarProdutosPorVendedor(int vendedorId) throws SQLException {
        String sql = "SELECT * FROM produto WHERE vendedor_id = ?";
        try(Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, vendedorId);
            ResultSet rs = stmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            while(rs.next()){
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getInt("vendedor_id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("descricao"),
                        rs.getInt("estoque"),
                        rs.getBoolean("ativo"),
                        rs.getString("categoria")
                );
                produtos.add(p);
            }
            return produtos;
        }
    }

    public Produto buscarProdutoPorId(int vendedorId, int produtoId) throws SQLException {
        String sql = "SELECT * FROM produto WHERE vendedor_id = ? AND id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            stmt.setInt(2, produtoId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getInt("vendedor_id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("descricao"),
                        rs.getInt("estoque"),
                        rs.getBoolean("ativo"),
                        rs.getString("categoria")
                );
                return p;
            } else {
                return null;
            }
        }
    }

    public boolean desativarProduto(int vendedorId, int produtoId) throws SQLException {
        String sql = "UPDATE produto SET ativo = false WHERE vendedor_id = ? AND id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            stmt.setInt(2, produtoId);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public Produto atualizarProduto(int produtoId, int vendedorId, Produto produtoAtualizado) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco = ?, descricao = ?, estoque = ?, categoria = ? " +
                "WHERE id = ? AND vendedor_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produtoAtualizado.getNome());
            stmt.setDouble(2, produtoAtualizado.getPreco());
            stmt.setString(3, produtoAtualizado.getDescricao());
            stmt.setInt(4, produtoAtualizado.getEstoque());
            stmt.setString(5, produtoAtualizado.getCategoria());
            stmt.setInt(6, produtoId);
            stmt.setInt(7, vendedorId);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                // Nenhuma linha foi atualizada: produto não existe ou não pertence ao vendedor
                return null; // ou lançar exceção se preferir
            }

            // Retorna o objeto atualizado com ID e vendedorId
            produtoAtualizado.setId(produtoId);
            produtoAtualizado.setVendedorId(vendedorId);
            return produtoAtualizado;
        }
    }

    public boolean deletarProduto(int produtoId) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }



}
