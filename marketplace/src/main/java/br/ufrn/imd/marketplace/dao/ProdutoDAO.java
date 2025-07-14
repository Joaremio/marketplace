package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.Imagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {

    @Autowired
    private ImagemDAO imagemDAO;

    @Autowired
    private DB_Connection dbConnection;

    public ProdutoImagemDTO cadastrarProduto(int vendedorId, ProdutoImagemDTO produto) throws SQLException {
        String sql = "INSERT INTO produto (vendedor_id, nome, preco, descricao, estoque, categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, vendedorId);
                stmt.setString(2, produto.getNome());
                stmt.setDouble(3, produto.getPreco());
                stmt.setString(4, produto.getDescricao());
                stmt.setInt(5, produto.getEstoque());
                stmt.setString(6, produto.getCategoria());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setId(rs.getInt(1));
                    } else {
                        throw new SQLException("Erro ao obter ID do produto.");
                    }
                }
            }

            Imagem imagem = new Imagem(produto.getImagemUrl(), produto.getId());
            imagemDAO.salvarImagem(conn, imagem);

            conn.commit();
            produto.setVendedorId(vendedorId);
            return produto;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<ProdutoImagemDTO> buscarProdutosAtivos(String nome, String categoria) throws SQLException {
        List<ProdutoImagemDTO> produtos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.vendedor_id, p.nome, p.preco, p.descricao, p.estoque, p.categoria, i.imagem AS imageUrl
            FROM produto p
            LEFT JOIN imagem i ON p.id = i.produto_id
            WHERE p.ativo = true AND p.estoque > 0
        """);

        List<Object> params = new ArrayList<>();
        if (nome != null && !nome.trim().isEmpty()) {
            sql.append(" AND p.nome LIKE ?");
            params.add("%" + nome + "%");
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            sql.append(" AND p.categoria = ?");
            params.add(categoria);
        }

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProdutoImagemDTO produto = new ProdutoImagemDTO(
                            rs.getInt("id"),
                            rs.getInt("vendedor_id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getString("descricao"),
                            rs.getInt("estoque"),
                            rs.getString("categoria"),
                            rs.getString("imageUrl")
                    );
                    produtos.add(produto);
                }
            }
        }
        return produtos;
    }

    public List<ProdutoImagemDTO> buscarProdutosPorVendedor(int vendedorId) throws SQLException {
        String sql = """
            SELECT p.id, p.vendedor_id, p.nome, p.preco, p.descricao, p.estoque, p.categoria, i.imagem AS imageUrl
            FROM produto p
            LEFT JOIN imagem i ON p.id = i.produto_id
            WHERE p.vendedor_id = ?
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            List<ProdutoImagemDTO> produtos = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProdutoImagemDTO produto = new ProdutoImagemDTO(
                            rs.getInt("id"),
                            rs.getInt("vendedor_id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getString("descricao"),
                            rs.getInt("estoque"),
                            rs.getString("categoria"),
                            rs.getString("imageUrl")
                    );
                    produtos.add(produto);
                }
            }
            return produtos;
        }
    }
    public ProdutoImagemDTO buscarProdutoPorId(int vendedorId, int produtoId) throws SQLException {
        String sql = """
            SELECT p.id, p.vendedor_id, p.nome, p.preco, p.descricao, p.estoque, p.categoria, i.imagem AS imageUrl
            FROM produto p
            LEFT JOIN imagem i ON p.id = i.produto_id
            WHERE p.vendedor_id = ? AND p.id = ?
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            stmt.setInt(2, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProdutoImagemDTO(
                            rs.getInt("id"),
                            rs.getInt("vendedor_id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getString("descricao"),
                            rs.getInt("estoque"),
                            rs.getString("categoria"),
                            rs.getString("imageUrl")
                    );
                }
            }
        }
        return null;
    }

    public ProdutoImagemDTO buscarProdutoPublicoPorId(int produtoId) throws SQLException {
        String sql = """
            SELECT p.id, p.vendedor_id, p.nome, p.preco, p.descricao, p.estoque, p.categoria, i.imagem AS imageUrl
            FROM produto p
            LEFT JOIN imagem i ON p.id = i.produto_id
            WHERE p.id = ? AND p.ativo = true AND p.estoque > 0
        """;
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProdutoImagemDTO(
                        rs.getInt("id"), rs.getInt("vendedor_id"), rs.getString("nome"),
                        rs.getDouble("preco"), rs.getString("descricao"), rs.getInt("estoque"),
                        rs.getString("categoria"), rs.getString("imageUrl")
                    );
                }
            }
        }
        return null;
    }

    public boolean desativarProduto(int vendedorId, int produtoId) throws SQLException {
        String sql = "UPDATE produto SET ativo = false WHERE vendedor_id = ? AND id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendedorId);
            stmt.setInt(2, produtoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public ProdutoImagemDTO atualizarProduto(int produtoId, int vendedorId, ProdutoImagemDTO produtoAtualizado) throws SQLException {
        String sql = """
            UPDATE produto SET nome = ?, preco = ?, descricao = ?, estoque = ?, categoria = ?
            WHERE id = ? AND vendedor_id = ?
        """;

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
            if (linhasAfetadas == 0) return null;

            imagemDAO.deletarImagensDoProduto(conn, produtoId);
            imagemDAO.salvarImagem(conn, new Imagem(produtoAtualizado.getImagemUrl(), produtoId));

            produtoAtualizado.setId(produtoId);
            produtoAtualizado.setVendedorId(vendedorId);
            return produtoAtualizado;
        }
    }

    public boolean deletarProduto(int produtoId) throws SQLException {
        Connection conn = dbConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            imagemDAO.deletarImagensDoProduto(conn, produtoId);

            String sql = "DELETE FROM produto WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, produtoId);
                int linhasAfetadas = stmt.executeUpdate();
                conn.commit();
                return linhasAfetadas > 0;
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public ProdutoImagemDTO buscarProdutoPorId(int produtoId) throws SQLException {
        String sql = """
            SELECT p.id, p.vendedor_id, p.nome, p.preco, p.descricao, p.estoque, p.categoria, i.imagem AS imageUrl
            FROM produto p
            LEFT JOIN imagem i ON p.id = i.produto_id
            WHERE p.id = ? AND p.ativo = true
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProdutoImagemDTO(
                            rs.getInt("id"),
                            rs.getInt("vendedor_id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getString("descricao"),
                            rs.getInt("estoque"),
                            rs.getString("categoria"),
                            rs.getString("imageUrl")
                    );
                }
            }
        }
        return null;
    }
}
