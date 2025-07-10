package br.ufrn.imd.marketplace.dao;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.ListaDesejos;
import br.ufrn.imd.marketplace.model.ListaProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ListaDesejosDAO {

    @Autowired
    private DB_Connection dbConnection;

    public ListaDesejos criarLista(ListaDesejos lista) throws SQLException {
        String sql = "INSERT INTO lista_desejos(nome, comprador_id) VALUES (?,?)";
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, lista.getNome());
            stmt.setInt(2, lista.getCompradorId());
            stmt.executeUpdate();

            // recupera o id gerado pelo banco
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    lista.setId(idGerado);
                }
            }
            return lista;
        }
    }

    public ListaDesejos getListaDesejosById(int listaId) throws SQLException {
        String sql = "SELECT * FROM lista_desejos WHERE id = ?";
        try(Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, listaId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int idGerado = rs.getInt("id");
                String nome = rs.getString("nome");
                int compradorId = rs.getInt("comprador_id");
                return new ListaDesejos(idGerado, nome, compradorId);
            }
            return null;
        }
    }

    public void adicionarProdutoAListaDesejos(ListaProduto produto ) throws SQLException {
        String sql = "INSERT into lista_produtos(PRODUTO_id,LISTA_DESEJOS_id) VALUES (?,?)";
        try(Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, produto.getProdutoId());
            stmt.setInt(2, produto.getListaId());
            stmt.executeUpdate();
        }
    }

    public List<Integer> getProdutoIdsDaLista(int listaId) throws SQLException {
        String sql = "SELECT PRODUTO_id FROM lista_produtos WHERE LISTA_DESEJOS_id = ?";
        List<Integer> produtoIds = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, listaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produtoIds.add(rs.getInt("PRODUTO_id"));
            }
        }

        return produtoIds;
    }


    public List<ProdutoImagemDTO> buscarProdutosCompletosDaLista(int listaId) throws SQLException {
        List<ProdutoImagemDTO> produtos = new ArrayList<>();

        // Esta é a query que faz a "mágica", unindo as 3 tabelas
        String sql = "SELECT " +
                "  p.id, p.vendedor_id, p.nome, p.preco, p.descricao, p.estoque, p.categoria, i.imagem AS imageUrl " +
                "FROM lista_produtos lp " +
                "JOIN produto p ON lp.PRODUTO_id = p.id " +
                "LEFT JOIN imagem i ON p.id = i.produto_id " + // LEFT JOIN para incluir produtos mesmo sem imagem
                "WHERE lp.LISTA_DESEJOS_id = ? AND p.ativo = true";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, listaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Para cada linha do resultado, criamos um DTO completo
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

    // Adicione este método para buscar todas as listas de um comprador
    public List<ListaDesejos> buscarListasPorComprador(int compradorId) throws SQLException {
        List<ListaDesejos> listas = new ArrayList<>();
        String sql = "SELECT * FROM lista_desejos WHERE comprador_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, compradorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listas.add(new ListaDesejos(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("comprador_id")
                ));
            }
        }
        return listas;
    }

    public void atualizarNomeLista(int listaId, String novoNome) throws SQLException {
        String sql = "UPDATE lista_desejos SET nome = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoNome);
            stmt.setInt(2, listaId);
            stmt.executeUpdate();
        }
    }

    // No arquivo ListaDesejosDAO.java

    public void excluirLista(int listaId) throws SQLException {
        String sqlItens = "DELETE FROM lista_produtos WHERE LISTA_DESEJOS_id = ?";
        String sqlLista = "DELETE FROM lista_desejos WHERE id = ?";

        // Usaremos uma única conexão para garantir a transação
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens);
                 PreparedStatement stmtLista = conn.prepareStatement(sqlLista)) {

                // --- Depuração do primeiro DELETE ---
                System.out.println("DAO: Tentando excluir itens para a lista_id: " + listaId);
                stmtItens.setInt(1, listaId);
                int linhasAfetadasItens = stmtItens.executeUpdate(); // Captura o número de linhas afetadas
                System.out.println("DAO: Linhas removidas da tabela 'lista_produtos': " + linhasAfetadasItens);

                // --- Depuração do segundo DELETE ---
                System.out.println("DAO: Tentando excluir a lista principal com id: " + listaId);
                stmtLista.setInt(1, listaId);
                int linhasAfetadasLista = stmtLista.executeUpdate(); // Captura o número de linhas afetadas
                System.out.println("DAO: Linhas removidas da tabela 'lista_desejos': " + linhasAfetadasLista);

                // Se nenhuma lista principal foi removida, é um sinal de que o ID pode estar errado
                if (linhasAfetadasLista == 0) {
                    System.out.println("AVISO: Nenhuma lista de desejos foi encontrada no banco de dados com o ID " + listaId + ". A transação será desfeita (rollback).");
                    conn.rollback(); // Desfaz a exclusão dos itens, pois a lista principal não foi encontrada
                    return; // Encerra o método
                }

                conn.commit(); // Confirma a transação apenas se a lista principal foi excluída
                System.out.println("DAO: Transação confirmada (commit) com sucesso.");

            } catch (SQLException e) {
                System.err.println("DAO: Ocorreu um erro SQL, desfazendo a transação (rollback).");
                conn.rollback();
                throw e;
            }
        }
    }

}
