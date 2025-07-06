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
                        rs.getInt("id"), rs.getInt("vendedor_id"), rs.getString("nome"),
                        rs.getDouble("preco"), rs.getString("descricao"), rs.getInt("estoque"),
                        rs.getString("categoria"), rs.getString("imageUrl")
                    );
                    produtos.add(produto);
                }
            }
        }
        return produtos;
    }


    public ProdutoImagemDTO cadastrarProduto(int vendedorId, ProdutoImagemDTO produto) throws SQLException {
        String sql = "INSERT INTO produto (vendedor_id, nome, preco, descricao, estoque, categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Use uma transação para garantir a consistência entre as tabelas 'produto' e 'imagem'
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // --- PONTO CRÍTICO DA CORREÇÃO ---
                // A linha abaixo define o valor para o "parâmetro 1"
                // É provável que ela esteja faltando no seu código atual.
                stmt.setInt(1, vendedorId);
                // ---------------------------------

                stmt.setString(2, produto.getNome());
                stmt.setDouble(3, produto.getPreco());
                stmt.setString(4, produto.getDescricao());
                stmt.setInt(5, produto.getEstoque());
                stmt.setString(6, produto.getCategoria());

                stmt.executeUpdate();

                // Recupera o ID gerado para o produto
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setId(rs.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID do produto, nenhum ID foi gerado.");
                    }
                }
            }

            // Salva a imagem usando o ID do produto recém-criado
            // Assumindo que seu ImagemDAO precisa da conexão para a transação
            Imagem imagem = new Imagem(produto.getImagemUrl(), produto.getId());
            imagemDAO.salvarImagem(conn, imagem); // Você precisará criar este método que recebe a conexão

            conn.commit(); // Confirma a transação se tudo deu certo

            produto.setVendedorId(vendedorId);
            return produto;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Desfaz a transação em caso de erro
            }
            throw e; // Relança a exceção para ser tratada pela camada de serviço/controller
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }



    public List<ProdutoImagemDTO> buscarProdutosPorVendedor(int vendedorId) throws SQLException {
        // Query SQL com o alias corrigido para 'imageUrl' para alinhar com o frontend
        String sql = """
        SELECT 
            p.id,
            p.vendedor_id,
            p.nome,
            p.preco,
            p.descricao,
            p.estoque,
            p.categoria,
            i.imagem AS imageUrl 
        FROM 
            produto p
        LEFT JOIN 
            imagem i ON p.id = i.produto_id
        WHERE 
            p.vendedor_id = ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            List<ProdutoImagemDTO> produtos = new ArrayList<>();

            // Usando try-with-resources para o ResultSet garantir o fechamento
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Instanciando o DTO com o campo 'imageUrl'
                    ProdutoImagemDTO produto = new ProdutoImagemDTO(
                            rs.getInt("id"),
                            rs.getInt("vendedor_id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getString("descricao"),
                            rs.getInt("estoque"),
                            rs.getString("categoria"),
                            rs.getString("imageUrl") // Lendo o alias corrigido
                    );
                    produtos.add(produto);
                }
            }
            return produtos;
        }
    }



    public ProdutoImagemDTO buscarProdutoPorId(int vendedorId, int produtoId) throws SQLException {
        // Query SQL com LEFT JOIN e o alias corrigido para 'imageUrl'
        String sql = """
        SELECT 
            p.id,
            p.vendedor_id,
            p.nome,
            p.preco,
            p.descricao,
            p.estoque,
            p.categoria,
            i.imagem AS imageUrl
        FROM 
            produto p
        LEFT JOIN 
            imagem i ON p.id = i.produto_id
        WHERE 
            p.vendedor_id = ? AND p.id = ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            stmt.setInt(2, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Instanciando o DTO, que inclui a URL da imagem
                    return new ProdutoImagemDTO(
                            rs.getInt("id"),
                            rs.getInt("vendedor_id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getString("descricao"),
                            rs.getInt("estoque"),
                            rs.getString("categoria"),
                            rs.getString("imageUrl") // Lendo o alias corrigido
                    );
                } else {
                    return null;
                }
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


    public ProdutoImagemDTO atualizarProduto(int produtoId, int vendedorId, ProdutoImagemDTO produtoAtualizado) throws SQLException {
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
                return null; // Produto não encontrado ou não pertence ao vendedor
            }

            // Atualiza a imagem (aqui substituindo a antiga por nova)
            Imagem imagem = new Imagem(produtoAtualizado.getImagemUrl(), produtoId);
            imagemDAO.deletarImagensDoProduto(conn,produtoId);
            imagemDAO.salvarImagem(conn,imagem);

            produtoAtualizado.setId(produtoId);
            produtoAtualizado.setVendedorId(vendedorId);
            return produtoAtualizado;
        }
    }


    public boolean deletarProduto(int produtoId) throws SQLException {
        // Envolver em uma transação, seguindo o exemplo acima
        Connection conn = dbConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            // 1. Deletar as imagens associadas
            imagemDAO.deletarImagensDoProduto(conn, produtoId);

            // 2. Deletar o produto
            String sql = "DELETE FROM produto WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, produtoId);
                int linhasAfetadas = stmt.executeUpdate();

                conn.commit(); // Sucesso, confirma a transação
                return linhasAfetadas > 0;
            }
        } catch (SQLException e) {
            conn.rollback(); // Erro, desfaz tudo
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }



}
