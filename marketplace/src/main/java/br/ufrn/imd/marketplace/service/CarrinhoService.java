package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dao.CarrinhoDAO;
import br.ufrn.imd.marketplace.dao.ProdutoDAO;
import br.ufrn.imd.marketplace.dto.ProdutoCarrinhoDetalhado;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
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
    private ProdutoDAO produtoDAO;

    @Autowired
    private DB_Connection dbConnection;

    public Carrinho criarCarrinho(int compradorId) {
        try {
            Connection conn = dbConnection.getConnection();
            return carrinhoDAO.criarCarrinho(conn, compradorId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void inserirProduto(CarrinhoProduto produto) {
        try {
            ProdutoImagemDTO produtoInfo = produtoDAO.buscarProdutoPorId(produto.getProdutoId());
            
            if (produtoInfo == null) {
                throw new RuntimeException("Produto não encontrado");
            }

            if (carrinhoDAO.produtoExisteNoCarrinho(produto.getProdutoId(), produto.getCarrinhoId())) {
                int quantidadeAtual = carrinhoDAO.obterQuantidadeDoProduto(produto.getCarrinhoId(), produto.getProdutoId());
                int novaQuantidade = quantidadeAtual + produto.getQuantidade();
                
                if (novaQuantidade > produtoInfo.getEstoque()) {
                    throw new RuntimeException("Quantidade solicitada (" + novaQuantidade + 
                        ") excede o estoque disponível (" + produtoInfo.getEstoque() + ")");
                }
                
                produto.setQuantidade(novaQuantidade);
                carrinhoDAO.atualizarQuantidade(produto);
            } else {
                if (produto.getQuantidade() > produtoInfo.getEstoque()) {
                    throw new RuntimeException("Quantidade solicitada (" + produto.getQuantidade() + 
                        ") excede o estoque disponível (" + produtoInfo.getEstoque() + ")");
                }
                
                carrinhoDAO.inserirProduto(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto no carrinho", e);
        }
    }

    public Carrinho buscarCarrinhoCompletoPorUsuarioId(int usuarioId) {
        try {
            Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);

            if (carrinho != null) {
                List<ProdutoCarrinhoDetalhado> produtos = carrinhoDAO.obterProdutosDetalhadosDoCarrinho(carrinho.getId());
                carrinho.setProdutos(produtos);
            }

            return carrinho;
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco de dados ao buscar carrinho completo.", e);
        }
    }

    public List<ProdutoCarrinhoDetalhado> getProdutos(int carrinhoId) {
        try {
            List<ProdutoCarrinhoDetalhado> produtos = carrinhoDAO.obterProdutosDetalhadosDoCarrinho(carrinhoId);
            if (produtos.isEmpty()) {
                throw new RuntimeException("Carrinho está vazio");
            }
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Carrinho> listarCarrinhos() {
        try {
            List<Carrinho> carrinhos = carrinhoDAO.listarTodos();
            if (carrinhos.isEmpty()) {
                throw new RuntimeException("Não possui carrinhos");
            }
            return carrinhos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Carrinho> getCarrinhoByID(int usuarioId) {
        try {
            Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);
            return Optional.ofNullable(carrinho);
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco de dados ao buscar carrinho", e);
        }
    }

    public void removerProduto(int usuarioId, int produtoId) {
        try {
            Carrinho carrinho = carrinhoDAO.getCarrinhoPorId(usuarioId);

            if (carrinho != null) {
                carrinhoDAO.removerProdutoDoCarrinho(carrinho.getId(), produtoId);
            } else {
                throw new RuntimeException("Carrinho não encontrado para o usuário com ID: " + usuarioId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto do carrinho", e);
        }
    }

    public void atualizarQuantidade(CarrinhoProduto produto) {
        try {
            ProdutoImagemDTO produtoInfo = produtoDAO.buscarProdutoPorId(produto.getProdutoId());
            
            if (produtoInfo == null) {
                throw new RuntimeException("Produto não encontrado");
            }

            if (produto.getQuantidade() > produtoInfo.getEstoque()) {
                throw new RuntimeException("Quantidade solicitada (" + produto.getQuantidade() + 
                    ") excede o estoque disponível (" + produtoInfo.getEstoque() + ")");
            }

            if (produto.getQuantidade() <= 0) {
                throw new RuntimeException("Quantidade deve ser maior que zero");
            }

            carrinhoDAO.atualizarQuantidade(produto);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quantidade do produto", e);
        }
    }

    public void esvaziarCarrinho(int carrinhoId) {
        try {
            carrinhoDAO.removerTodosProdutosDoCarrinho(carrinhoId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao esvaziar carrinho", e);
        }
    }



}