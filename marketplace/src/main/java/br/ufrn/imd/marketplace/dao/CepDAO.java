package br.ufrn.imd.marketplace.dao;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Cep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CepDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void salvarOuAtualizar(Cep cep) throws SQLException {
    String sql = """
        INSERT INTO cep (cep, logradouro, bairro, cidade, estado)
        VALUES (?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            logradouro = VALUES(logradouro),
            bairro = VALUES(bairro),
            cidade = VALUES(cidade),
            estado = VALUES(estado)
    """;
    
    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, cep.getCep());
        stmt.setString(2, cep.getLogradouro());
        stmt.setString(3, cep.getBairro());
        stmt.setString(4, cep.getCidade());
        stmt.setString(5, cep.getEstado());
        
        stmt.executeUpdate();
    }
}

    public List<Cep> listarCeps() throws SQLException {
        String sql = "select * from cep";
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            ResultSet rs = stmt.executeQuery();
            List<Cep> ceps = new ArrayList<>();
            while(rs.next()){
                ceps.add(new Cep(
                        rs.getString("cep"),
                        rs.getString("logradouro"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado")
                ));
            }
            return ceps;
        }
    }

    public Cep buscarCep(String cep) throws SQLException {
        String sql = "select * from cep where cep = ?";
        try(Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, cep);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Cep(
                        rs.getString("cep"),
                        rs.getString("logradouro"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado")
                );
            }
            return null;
        }
    }

    public boolean excluirCep(String cep) throws SQLException {
        String sql = "delete from cep where cep = ?";
        try(Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, cep);
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        }
    }

    

    public boolean cepExiste(String cep) {
        String sql = "SELECT 1 FROM cep WHERE cep = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cep);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência do CEP", e);
        }
    }

}
