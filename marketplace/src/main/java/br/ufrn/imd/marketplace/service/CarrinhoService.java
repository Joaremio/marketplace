package br.ufrn.imd.marketplace.service;


import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dao.CarrinhoDAO;
import br.ufrn.imd.marketplace.dto.ProdutoCarrinhoDetalhado;
import br.ufrn.imd.marketplace.model.Carrinho;
import br.ufrn.imd.marketplace.model.CarrinhoProduto;
import br.ufrn.imd.marketplace.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoDAO carrinhoDAO;

    @Autowired
    private DB_Connection dbConnection;

    public Carrinho criarCarrinho(int compradorId) {
        try {
            Connection conn = dbConnection.getConnection();
            return carrinhoDAO.criarCarrinho(conn,compradorId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void inserirProduto(CarrinhoProduto produto) {
        try {
            if (carrinhoDAO.produtoExisteNoCarrinho(produto.getProdutoId(), produto.getCarrinhoId())) {
                // Busca a quantidade atual no carrinho
                int quantidadeAtual = carrinhoDAO.obterQuantidadeDoProduto(produto.getCarrinhoId(), produto.getProdutoId());

                // Soma a nova quantidade
                int novaQuantidade = quantidadeAtual + produto.getQuantidade();
                produto.setQuantidade(novaQuantidade);

                // Atualiza a quantidade no banco
                carrinhoDAO.atualizarQuantidade(produto);
            } else {
                // Insere novo produto no carrinho
                carrinhoDAO.inserirProduto(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto no carrinho", e);
        }
    }

    public void removerProduto(int carrinhoId, int produtoId) {
        try{
            carrinhoDAO.removerProdutoDoCarrinho(carrinhoId, produtoId);
        }catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto no carrinho", e);
        }
    }

    public List<ProdutoCarrinhoDetalhado> getProdutos(int carrinhoId){
        try{
            List<ProdutoCarrinhoDetalhado> produtos = carrinhoDAO.obterProdutosDetalhadosDoCarrinho(carrinhoId);
            if(produtos.isEmpty()){
                throw new RuntimeException("Carrinho está vazio");
            }
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Carrinho> listarCarrinhos() {
        try{
            List<Carrinho> carrinhos = carrinhoDAO.listarTodos();
            if(carrinhos.isEmpty()){
                throw new RuntimeException("Não possui carrinhos");
            }
            return carrinhos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

