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
                int quantidadeAtual = carrinhoDAO.obterQuantidadeDoProduto(produto.getCarrinhoId(), produto.getProdutoId());
                int novaQuantidade = quantidadeAtual + produto.getQuantidade();
                produto.setQuantidade(novaQuantidade);
                carrinhoDAO.atualizarQuantidade(produto);
            } else {
                carrinhoDAO.inserirProduto(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto no carrinho", e);
        }
    }

    public Carrinho buscarCarrinhoCompletoPorUsuarioId(int usuarioId) {
        try {
            // 1. Busca o "casco" do carrinho
            Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);

            // 2. Se o carrinho existir, busca seus produtos detalhados
            if (carrinho != null) {
                List<ProdutoCarrinhoDetalhado> produtos = carrinhoDAO.obterProdutosDetalhadosDoCarrinho(carrinho.getId());
                // 3. Anexa a lista de produtos ao objeto carrinho
                carrinho.setProdutos(produtos);
            }

            return carrinho;
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco de dados ao buscar carrinho completo.", e);
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

    public void removerProduto(int usuarioId, int produtoId) {
        try {
            // 1. Encontra o carrinho do usuário para obter o carrinhoId
            Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);

            if (carrinho != null) {
                // 2. Com o carrinhoId em mãos, chama o DAO para remover o item específico
                carrinhoDAO.removerProdutoDoCarrinho(carrinho.getId(), produtoId);
            } else {
                throw new RuntimeException("Carrinho não encontrado para o usuário com ID: " + usuarioId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto do carrinho", e);
        }
    }

    // Em CarrinhoService.java

    public void atualizarQuantidade(CarrinhoProduto produto) {
        try {
            // O seu DAO já deve ter um método para isso, basta chamá-lo.
            carrinhoDAO.atualizarQuantidade(produto);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quantidade do produto", e);
        }
    }


}

