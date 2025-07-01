package br.ufrn.imd.marketplace.service;


import br.ufrn.imd.marketplace.dao.ImagemDAO;
import br.ufrn.imd.marketplace.model.Imagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ImagemService {
    @Autowired
    private ImagemDAO imagemDAO;

    public void salvarImagem(Imagem imagem)  {
        try{
            imagemDAO.salvarImagem(imagem);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Imagem> buscarImagensDoProduto(int idProduto) {
        try{
           if(imagemDAO.buscarImagensDoProduto(idProduto).isEmpty()){
               throw new RuntimeException("Não existe imagens para esse produto");
           }
           return imagemDAO.buscarImagensDoProduto(idProduto);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluirTodasImagens(int idProduto) {
        try{
            imagemDAO.deletarImagensDoProduto(idProduto);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluirImagem(Imagem imagem) {
        try{
            imagemDAO.deletarImagemDeProduto(imagem);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
