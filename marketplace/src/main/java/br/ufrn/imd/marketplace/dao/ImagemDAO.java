package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Imagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImagemDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void salvarImagem(Connection conn, Imagem imagem) throws SQLException {
        String sql = "INSERT INTO imagem (imagem, produto_id) VALUES (?, ?)";
        try(
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, imagem.getImagemCaminho());
            stmt.setInt(2, imagem.getProdutoId());
            stmt.executeUpdate();
        }
    }

    public List<Imagem> buscarImagensDoProduto(int produtoId) throws SQLException {
        String sql = "SELECT * FROM imagem WHERE produto_id = ?";
        List<Imagem> imagens = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Imagem img = new Imagem(
                        rs.getString("imagem"),
                        rs.getInt("produto_id")
                );
                imagens.add(img);
            }
        }

        return imagens;
    }

    public boolean deletarImagensDoProduto(Connection conn, int produtoId) throws SQLException {
        String sql = "DELETE FROM imagem WHERE produto_id = ?";
        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public boolean deletarImagemDeProduto(Imagem imagem) throws SQLException {
        String sql = "DELETE FROM imagem WHERE imagem = ? AND produto_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imagem.getImagemCaminho());
            stmt.setInt(2, imagem.getProdutoId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }




}
