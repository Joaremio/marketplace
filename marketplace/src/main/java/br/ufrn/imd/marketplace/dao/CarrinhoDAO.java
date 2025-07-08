package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.ProdutoCarrinhoDetalhado;
import br.ufrn.imd.marketplace.model.Carrinho;
import br.ufrn.imd.marketplace.model.CarrinhoProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CarrinhoDAO {

    @Autowired
    private DB_Connection dbConnection;


    public Carrinho criarCarrinho(Connection conn, int usuarioId) throws SQLException {
        String sql = "INSERT INTO carrinho (comprador_id) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();

            // Pegando o ID gerado automaticamente
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int carrinhoId = rs.getInt(1);

                // Criando e retornando o objeto Carrinho com ID e usuário
                Carrinho carrinho = new Carrinho();
                carrinho.setId(carrinhoId);
                carrinho.setCompradorId(usuarioId);
                return carrinho;
            } else {
                throw new SQLException("Falha ao criar carrinho: ID não retornado.");
            }
        }
    }

    public void inserirProduto(CarrinhoProduto item) throws SQLException {
        String sql = "INSERT INTO carrinho_produto (carrinho_id, produto_id, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getCarrinhoId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.executeUpdate();
        }
    }

    public boolean produtoExisteNoCarrinho(int produtoId, int carrinhoId) throws SQLException {
        String sql = "SELECT * FROM carrinho_produto WHERE PRODUTO_id= ? AND CARRINHO_id = ?";
        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            stmt.setInt(2, carrinhoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        }
    }

    public int obterQuantidadeDoProduto(int carrinhoId, int produtoId) throws SQLException {
        String sql = "SELECT quantidade FROM carrinho_produto WHERE carrinho_id = ? AND produto_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carrinhoId);
            stmt.setInt(2, produtoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantidade");
            } else {
                return 0;
            }
        }
    }

    public void atualizarQuantidade(CarrinhoProduto produto) throws SQLException {
        String sql = "UPDATE carrinho_produto SET quantidade = ? WHERE carrinho_id = ? AND produto_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produto.getQuantidade());
            stmt.setInt(2, produto.getCarrinhoId());
            stmt.setInt(3, produto.getProdutoId());
            stmt.executeUpdate();
        }
    }

    public void removerProdutoDoCarrinho(int carrinhoId, int produtoId) throws SQLException {
        String sql = "DELETE FROM CARRINHO_produto WHERE carrinho_id = ? AND produto_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carrinhoId);
            stmt.setInt(2, produtoId);
            stmt.executeUpdate();
        }
    }

    public List<ProdutoCarrinhoDetalhado> obterProdutosDetalhadosDoCarrinho(int carrinhoId) throws SQLException {
        List<ProdutoCarrinhoDetalhado> produtos = new ArrayList<>();
        String sql = """
        SELECT cp.produto_id, cp.quantidade, p.nome, p.preco, i.imagem
        FROM carrinho_produto cp
        JOIN produto p ON cp.produto_id = p.id
        LEFT JOIN imagem i ON i.produto_id = p.id
        WHERE cp.carrinho_id = ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carrinhoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProdutoCarrinhoDetalhado item = new ProdutoCarrinhoDetalhado();
                item.setProdutoId(rs.getInt("produto_id"));
                item.setNome(rs.getString("nome"));
                item.setPreco(rs.getDouble("preco"));
                item.setImagemUrl(rs.getString("imagem"));
                item.setQuantidade(rs.getInt("quantidade"));

                produtos.add(item);
            }
        }
        return produtos;
    }




    public List<Carrinho> listarTodos() throws SQLException {
        List<Carrinho> carrinhos = new ArrayList<>();

        String sql = "SELECT * FROM carrinho";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Carrinho carrinho = new Carrinho();
                carrinho.setId(rs.getInt("id"));
                carrinho.setCompradorId(rs.getInt("comprador_id"));

                carrinhos.add(carrinho);
            }
        }

        return carrinhos;
    }

    public Carrinho getCarrinhoPorId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM carrinho WHERE comprador_id = ?";

        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Carrinho carrinho = new Carrinho();
                carrinho.setId(rs.getInt("id"));
                carrinho.setCompradorId(rs.getInt("comprador_id"));
                return carrinho;
            }
            return null;
        }
    }



}
