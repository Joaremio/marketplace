package br.ufrn.imd.marketplace.service;


import br.ufrn.imd.marketplace.dao.ProdutoDAO;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoDAO produtoDAO;


    public ProdutoImagemDTO cadastrarProduto(int vendedorId, ProdutoImagemDTO produto) {
        try{
           return produtoDAO.cadastrarProduto(vendedorId, produto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoImagemDTO> buscarProdutosParaVitrine(String nome, String categoria) {
    try {
        return produtoDAO.buscarProdutosAtivos(nome, categoria);
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar produtos para vitrine", e);
    }
}

public ProdutoImagemDTO buscarProdutoPublicoPorId(int produtoId) {
        try {
            ProdutoImagemDTO produto = produtoDAO.buscarProdutoPublicoPorId(produtoId);
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado ou indisponível.");
            }
            return produto;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto.", e);
        }
    }
    public List<ProdutoImagemDTO> buscarProdutosPorVendedor(int vendedorId) {
        try{
            List<ProdutoImagemDTO> produtos = produtoDAO.buscarProdutosPorVendedor(vendedorId);
            if(produtos.isEmpty()){
                throw new RuntimeException("Vendedor nao possui produtos cadastrados.");
            }
            return produtos;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Produto buscarProdutoPorId(int vendedorId, int produtoId) {
        try{
            Produto produto = produtoDAO.buscarProdutoPorId(vendedorId,produtoId);
            if(produto == null){
                throw new RuntimeException("Produto nao possui produto cadastrado.");
            }
            return produto;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void desativarProduto(int vendedorId, int produtoId) {
        try {
            boolean sucesso = produtoDAO.desativarProduto(vendedorId, produtoId);
            if (!sucesso) {
                throw new RuntimeException("Produto não encontrado ou não pertence ao vendedor.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar o banco de dados ao desativar produto.", e);
        }
    }

    public ProdutoImagemDTO atualizarProduto(int produtoId, int vendedorId, ProdutoImagemDTO produtoAtualizado){
        try{
            ProdutoImagemDTO produto = produtoDAO.atualizarProduto(produtoId, vendedorId, produtoAtualizado);
            if(produto == null){
                throw new RuntimeException("Produto não encontrado ou não pertence ao vendedor.");
            }
            return produto;
        }catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar produto.", e);
        }
    }

    public void deletarProduto(int produtoId) {
        try{
            if(!produtoDAO.deletarProduto(produtoId)){
                throw new RuntimeException("Erro ao deletar produto.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
