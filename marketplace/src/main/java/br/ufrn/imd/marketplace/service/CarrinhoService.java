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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public Optional<Carrinho> getCarrinhoByID(int usuarioId) {
        try {
            // O DAO já retorna null se não encontrar, o que é perfeito para o Optional.
            Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);

            // Converte o resultado (que pode ser null) em um Optional.
            return Optional.ofNullable(carrinho);
        } catch (SQLException e) {
            // Para erros de SQL reais, ainda lançamos uma exceção de runtime.
            throw new RuntimeException("Erro de banco de dados ao buscar carrinho", e);
        }
    }

    // Em CarrinhoService.java

public void atualizarQuantidadeProduto(int carrinhoId, int produtoId, int novaQuantidade) {
    try {
        if (novaQuantidade <= 0) {
            removerProduto(carrinhoId, produtoId);
        } else {
            carrinhoDAO.atualizarQuantidade(carrinhoId, produtoId, novaQuantidade);
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao atualizar quantidade no carrinho", e);
    }
}

public List<ProdutoCarrinhoDetalhado> getProdutosDetalhadosPorUsuario(int usuarioId) {
    try {
        Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);
        if (carrinho == null) {
            // Se o usuário não tem carrinho, retorna uma lista vazia, não um erro.
            return new ArrayList<>();
        }
        return carrinhoDAO.obterProdutosDetalhadosDoCarrinho(carrinho.getId());
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar produtos do carrinho", e);
    }
}


}

