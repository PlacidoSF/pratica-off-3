package servidor;

import java.util.ArrayList;
import java.util.List;

import estruturas.listaLigada.*;
import estruturas.hash.*;
import estruturas.arvoreSplay.ArvoreSplay;
import model.Filme;

public class Servidor {
    private ListaLigada dados;
    private TabelaHash<NoLista> index;
    private ArvoreSplay popularidadeGlobal;

    public Servidor() {
        this.dados = new ListaLigada();
        this.index = new TabelaHash<>(173);
        this.popularidadeGlobal = new ArvoreSplay();
    }

    public void cadastrarFilme(Filme filmeValor) {
        NoLista novoNo = this.dados.inserir(filmeValor);
        this.index.inserir(filmeValor.getId(), novoNo);
    }

    public Filme buscarComIndice(int id) {
        NoLista resultado = this.index.buscar(id);
        if (resultado != null) {
            Filme filmeEncontrado = resultado.getValorFilme();
            
            this.popularidadeGlobal.inserir(filmeEncontrado);
            
            return filmeEncontrado;
        }
        return null;
    }

    public Filme buscarSemIndice(int id) {
        NoLista resultado = this.dados.buscar(id);
        if (resultado != null) {
            Filme filmeEncontrado = resultado.getValorFilme();
            
            this.popularidadeGlobal.inserir(filmeEncontrado);
            
            return filmeEncontrado;
        }
        return null;
    }

    public void exibirPopularidadeGlobal() {
        this.popularidadeGlobal.exibirMaisPopulares();
    }

    public int getComparacoesIndex() {
        return this.index.getComparacoesBusca();
    }

    public int getComparacoesFisica() {
        return this.dados.getComparacoesBusca();
    }

    public List<Filme> obterPagina(int numeroPagina, int tamanhoPagina) {
        List<Filme> pagina = new ArrayList<>();
        int indiceInicio = (numeroPagina - 1) * tamanhoPagina;
        int contador = 0;
        
        NoLista atual = this.dados.getList();
        
        while (atual != null && pagina.size() < tamanhoPagina) {
            if (contador >= indiceInicio) {
                pagina.add(atual.getValorFilme());
            }
            atual = atual.getProxNo();
            contador++;
        }
        return pagina;
    }
}