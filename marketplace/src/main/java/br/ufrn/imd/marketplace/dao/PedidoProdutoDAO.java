package br.ufrn.imd.marketplace.dao;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import br.ufrn.imd.marketplace.service.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PedidoProdutoDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void adicionarItemAoPedido(PedidoProduto item) throws SQLException {
        String sql = "INSERT INTO pedido_produto (PEDIDO_id, PRODUTO_id, quantidade, preco_unitario) " +
                "VALUES (?, ?, ?, ?)";
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,item.getPedidoId());
            stmt.setInt(2,item.getProdutoId());
            stmt.setInt(3,item.getQuantidade());
            stmt.setDouble(4,item.getPrecoUnidade());
            stmt.executeUpdate();
        }
    }

    public List<PedidoProduto> ListarItensDoPedido(int pedidoId) throws SQLException {
        List<PedidoProduto> itens = new ArrayList<PedidoProduto>();
        String sql = "SELECT * FROM pedido_produto WHERE PEDIDO_id = ?";
        try(Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,pedidoId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                PedidoProduto item = new PedidoProduto();
                item.setPedidoId(rs.getInt("PEDIDO_id"));
                item.setProdutoId(rs.getInt("PRODUTO_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnidade(rs.getDouble(("preco_unitario")));
                itens.add(item);
            }
        }
        return itens;
    }

    public void ExcluirItemAoPedido(int pedidoId, int itemId) throws SQLException {
        String sql = "DELETE from pedido_produto WHERE PEDIDO_id = ? AND PRODUTO_id = ?";
        try(Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,pedidoId);
            stmt.setInt(2,itemId);
            stmt.executeUpdate();
        }
    }

}
