package cliente;

import estruturas.hash.TabelaHash;
import estruturas.listaAutoajustavel.*;
import estruturas.arvoreSplay.*;
import model.Filme;
import servidor.Servidor;

public class Cliente {
    private String nomeUsuario;
    private Servidor servidor;
    
    // Estruturas do Cliente
    private TabelaHash<NoAutoAjustavel> cacheHash;
    private ListaAutoAjustavel cacheLRU;
    private ArvoreSplay preferencias;
    
    private static final int LIMITE_CACHE = 50;
    private int tamanhoAtual = 0;

    public Cliente(String nomeUsuario, Servidor servidor) {
        this.nomeUsuario = nomeUsuario;
        this.servidor = servidor;
        
        this.cacheHash = new TabelaHash<>(LIMITE_CACHE);
        this.cacheLRU = new ListaAutoAjustavel();
        this.preferencias = new ArvoreSplay();
    }

    public void solicitarFilme(int id) {
        System.out.println("===================================================");
        System.out.println("[BUSCA SOLICITADA] ID: " + id + " | Usuário: " + nomeUsuario);
        System.out.println("---------------------------------------------------");

        
        NoAutoAjustavel noCache = cacheHash.buscar(id);

        if (noCache != null) {
        
            Filme filme = noCache.getFilme();
            System.out.println("[HIT] Filme encontrado no Cache Local (Tabela Hash).");
            System.out.println("> Comparações na Hash: " + cacheHash.getComparacoesBusca());
            System.out.println("---------------------------------------------------");
            System.out.println(filme);

            
            cacheLRU.buscarMF(id);

            preferencias.inserir(filme);

            Filme recomendacao = preferencias.recomendarPorCategoria(filme.getCategoria(), filme.getId());
            
            if (recomendacao != null) {
                System.out.println("---------------------------------------------------");
                System.out.println("[RECOMENDAÇÃO PARA VOCÊ]");
                System.out.println("Porque você assistiu a filmes de " + filme.getCategoria() + ":");
                System.out.println("> " + recomendacao.getNome() + " (ID: " + recomendacao.getId() + ")");
            }

        } else {
    
            System.out.println("[MISS] Filme não encontrado no Cache Local.");
            System.out.println("[REDE] Preparando requisição ao servidor (Aguardando Huffman)...");
            System.out.println("---------------------------------------------------");

        }
            
            
    }

    public void atualizarCache(Filme filme) {

        if (tamanhoAtual == LIMITE_CACHE) {
            Filme filmeRemovido = cacheLRU.removerUltimo();
            
            if (filmeRemovido != null) {
                cacheHash.remover(filmeRemovido.getId());
                
                System.out.println("---------------------------------------------------");
                System.out.println("[AVISO DE CACHE] Limite máximo (" + LIMITE_CACHE + ") atingido. Aplicando regra LRU...");
                System.out.println("Removendo do cache: " + filmeRemovido.getNome() + " (ID: " + filmeRemovido.getId() + ")");
            }
        } else {
            tamanhoAtual++;
        }

        cacheLRU.inserir(filme);
        
        cacheHash.inserir(filme.getId(), cacheLRU.getPrimeiro());

        preferencias.inserir(filme);
    }

    public ListaAutoAjustavel getCacheLRU() {
        return cacheLRU;
    }

    public ArvoreSplay getPreferencias() {
        return preferencias;
    }
}