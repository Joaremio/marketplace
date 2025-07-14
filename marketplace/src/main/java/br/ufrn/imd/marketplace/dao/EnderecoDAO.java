package br.ufrn.imd.marketplace.dao;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.EnderecoCepDTO;
import br.ufrn.imd.marketplace.model.Cep;
import br.ufrn.imd.marketplace.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnderecoDAO {

    @Autowired
    private DB_Connection dbConnection;

    public Endereco inserirEndereco(int usuarioId, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO endereco(usuario_id, numero, complemento, tipo_endereco, endereco_principal, CEP_cep) VALUES(?,?,?,?,?,?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, usuarioId);
            stmt.setString(2, endereco.getNumeroEndereco());
            stmt.setString(3, endereco.getComplemento());
            stmt.setString(4, endereco.getTipoEndereco());
            stmt.setBoolean(5, endereco.isEnderecoPrincipal());
            stmt.setString(6, endereco.getCep());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    endereco.setId(idGerado);
                }
            }

            endereco.setUsuarioId(usuarioId);
            return endereco;
        }
    }

    public EnderecoCepDTO buscarEnderecoCompletoPorId(int enderecoId) throws SQLException {
        String sql = "SELECT e.id, e.usuario_id, e.numero, e.complemento, e.tipo_endereco, e.endereco_principal, e.CEP_cep, " +
                "c.logradouro, c.bairro, c.cidade, c.estado " +
                "FROM endereco e JOIN cep c ON e.CEP_cep = c.cep " +
                "WHERE e.id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enderecoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rs.getInt("id"));
                    endereco.setUsuarioId(rs.getInt("usuario_id"));
                    endereco.setNumeroEndereco(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setTipoEndereco(rs.getString("tipo_endereco"));
                    endereco.setEnderecoPrincipal(rs.getBoolean("endereco_principal"));
                    endereco.setCep(rs.getString("CEP_cep"));

                    Cep cep = new Cep(
                            rs.getString("CEP_cep"),
                            rs.getString("logradouro"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado")
                    );

                    EnderecoCepDTO dto = new EnderecoCepDTO();
                    dto.setEndereco(endereco);
                    dto.setCep(cep);

                    return dto;
                } else {
                    return null;
                }
            }
        }
    }

    public List<EnderecoCepDTO> buscarEnderecosPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT e.id, e.usuario_id, e.numero, e.complemento, e.tipo_endereco, e.endereco_principal, e.CEP_cep, " +
                "c.logradouro, c.bairro, c.cidade, c.estado " +
                "FROM endereco e JOIN cep c ON e.CEP_cep = c.cep " +
                "WHERE e.usuario_id = ?";

        List<EnderecoCepDTO> lista = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rs.getInt("id"));
                    endereco.setUsuarioId(rs.getInt("usuario_id"));
                    endereco.setNumeroEndereco(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setTipoEndereco(rs.getString("tipo_endereco"));
                    endereco.setEnderecoPrincipal(rs.getBoolean("endereco_principal"));
                    endereco.setCep(rs.getString("CEP_cep"));

                    Cep cep = new Cep(
                            rs.getString("CEP_cep"),
                            rs.getString("logradouro"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado")
                    );

                    EnderecoCepDTO dto = new EnderecoCepDTO();
                    dto.setEndereco(endereco);
                    dto.setCep(cep);

                    lista.add(dto);
                }
            }
        }

        return lista;
    }

    public List<EnderecoCepDTO> buscarEnderecoPorUsuarioEcep(int usuarioId, String cep) throws SQLException {
        String sql = "SELECT e.id, e.usuario_id, e.numero, e.complemento, e.tipo_endereco, e.endereco_principal, e.CEP_cep, " +
                "c.logradouro, c.bairro, c.cidade, c.estado " +
                "FROM endereco e JOIN cep c ON e.CEP_cep = c.cep " +
                "WHERE e.usuario_id = ? AND e.CEP_cep = ?";

        List<EnderecoCepDTO> lista = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setString(2, cep);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rs.getInt("id"));
                    endereco.setUsuarioId(rs.getInt("usuario_id"));
                    endereco.setNumeroEndereco(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setTipoEndereco(rs.getString("tipo_endereco"));
                    endereco.setEnderecoPrincipal(rs.getBoolean("endereco_principal"));
                    endereco.setCep(rs.getString("CEP_cep"));

                    Cep cepObj = new Cep(
                            rs.getString("CEP_cep"),
                            rs.getString("logradouro"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado")
                    );

                    EnderecoCepDTO dto = new EnderecoCepDTO();
                    dto.setEndereco(endereco);
                    dto.setCep(cepObj);

                    lista.add(dto);


                }
            }
        }
        return lista;
    }

    public EnderecoCepDTO buscarEnderecoPrincipal(int usuarioId) throws SQLException {
        String sql = "SELECT e.id, e.usuario_id, e.numero, e.complemento, e.tipo_endereco, e.endereco_principal, e.CEP_cep, " +
                "c.logradouro, c.bairro, c.cidade, c.estado " +
                "FROM endereco e JOIN cep c ON e.CEP_cep = c.cep " +
                "WHERE e.usuario_id = ? AND e.endereco_principal = true";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rs.getInt("id"));
                    endereco.setUsuarioId(rs.getInt("usuario_id"));
                    endereco.setNumeroEndereco(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setTipoEndereco(rs.getString("tipo_endereco"));
                    endereco.setEnderecoPrincipal(rs.getBoolean("endereco_principal"));
                    endereco.setCep(rs.getString("CEP_cep"));

                    Cep cep = new Cep(
                            rs.getString("CEP_cep"),
                            rs.getString("logradouro"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado")
                    );

                    EnderecoCepDTO dto = new EnderecoCepDTO();
                    dto.setEndereco(endereco);
                    dto.setCep(cep);
                    return dto;
                }
            }
        }

        return null;
    }

    public void desmarcarEnderecosPrincipais(int usuarioId) throws SQLException {
        String sql = "UPDATE endereco SET endereco_principal = false WHERE usuario_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
        }
    }

    public void deletarEndereco(int enderecoId) throws SQLException {
        String sql = "DELETE FROM endereco WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enderecoId);
            stmt.executeUpdate();
        }
    }

    public String buscarCepDoEndereco(int enderecoId) throws SQLException {
        String sql = "SELECT CEP_cep FROM endereco WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enderecoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("CEP_cep");
            }
            return null;
        }
    }

    public boolean cepEmUso(String cep) throws SQLException {
        String sql = "SELECT COUNT(*) FROM endereco WHERE CEP_cep = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cep);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public void deletarCep(String cep) throws SQLException {
        String sql = "DELETE FROM cep WHERE cep = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cep);
            stmt.executeUpdate();
        }
    }


    public void atualizarEndereco(Endereco endereco) throws SQLException {
        String sql = "UPDATE endereco SET numero = ?, complemento = ?, tipo_endereco = ?, endereco_principal = ?, CEP_cep = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco.getNumeroEndereco());
            stmt.setString(2, endereco.getComplemento());
            stmt.setString(3, endereco.getTipoEndereco());
            stmt.setBoolean(4, endereco.isEnderecoPrincipal());
            stmt.setString(5, endereco.getCep());
            stmt.setInt(6, endereco.getId());

            stmt.executeUpdate();
        }
    }


}
