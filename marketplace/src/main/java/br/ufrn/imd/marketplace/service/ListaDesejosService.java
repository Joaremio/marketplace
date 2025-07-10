package br.ufrn.imd.marketplace.service;


import br.ufrn.imd.marketplace.dao.ListaDesejosDAO;
import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.ListaDesejos;
import br.ufrn.imd.marketplace.model.ListaProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.List;

@Service
public class ListaDesejosService {

    @Autowired
    private ListaDesejosDAO listaDesejosDAO;

    public ListaDesejos criarListaDesejos(ListaDesejos lista) {
        try{
            ListaDesejos listaDesejos = listaDesejosDAO.criarLista(lista);
            return listaDesejos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ListaDesejos getListaDesejosById(int listaDesejosId)  {
        try{
            ListaDesejos listaDesejos = listaDesejosDAO.getListaDesejosById(listaDesejosId);
            if(listaDesejos == null){
                throw new RuntimeException("Lista de desejos vazia");
            }
            return listaDesejos;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void adicionarProdutoAListaDesejos(ListaProduto produto){
        try{
            listaDesejosDAO.adicionarProdutoAListaDesejos(produto);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getListaDesejosComProduto(int listaId){
        try{
            return listaDesejosDAO.getProdutoIdsDaLista(listaId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProdutoImagemDTO> buscarProdutosCompletosDaLista(int listaId) {
        try {
            return listaDesejosDAO.buscarProdutosCompletosDaLista(listaId);
        } catch (SQLException e) {
            // Trate o erro de forma apropriada, com logs, etc.
            throw new RuntimeException("Erro ao buscar detalhes da lista de desejos", e);
        }
    }

    public List<ListaDesejos> buscarListasPorComprador(int compradorId) {
        try {
            return listaDesejosDAO.buscarListasPorComprador(compradorId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar listas de desejos do comprador.", e);
        }
    }

    public void atualizarNomeLista(int listaId, String novoNome) {
        try {
            listaDesejosDAO.atualizarNomeLista(listaId, novoNome);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar nome da lista.", e);
        }
    }

    public void excluirLista(int listaId) {
        try {
            listaDesejosDAO.excluirLista(listaId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir lista.", e);
        }
    }
}
