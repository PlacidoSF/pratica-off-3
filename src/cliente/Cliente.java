package cliente;

import estruturas.hash.TabelaHash;
import estruturas.listaAutoajustavel.*;
import estruturas.arvoreSplay.ArvoreSplay;
import model.Filme;
import servidor.Servidor;
import rede.ConexaoRede;
import rede.PacoteDados;
import estruturas.huffman.ArvoreHuffman;

public class Cliente {
    private String nomeUsuario;
    private String senha;
    private Servidor servidor;
    private ConexaoRede rede;
    
    private TabelaHash<NoAutoAjustavel> cacheHash;
    private ListaAutoAjustavel cacheLRU;
    private ArvoreSplay preferencias;
    
    private static final int LIMITE_CACHE = 50;
    private int tamanhoAtual = 0;

    public Cliente(String nomeUsuario, String senha, Servidor servidor, ConexaoRede rede) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.servidor = servidor;
        this.rede = rede;

        this.cacheHash = new TabelaHash<>(LIMITE_CACHE);
        this.cacheLRU = new ListaAutoAjustavel();
        this.preferencias = new ArvoreSplay();
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenha() {
        return senha;
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

            
            String pedidoRec = "REC:" + filme.getCategoria();
            PacoteDados pacoteEnvio = rede.comprimirETransmitir(pedidoRec);
            
            PacoteDados pacoteResposta = servidor.receberRequisicao(pacoteEnvio);
            
            String recomendacoesServidor = ArvoreHuffman.decodificar(pacoteResposta.getBits(), pacoteResposta.getRaizDecodificacao());
            
            filtrarEExibirRecomendacao(recomendacoesServidor, filme.getCategoria(), false);

        } else {
    
            System.out.println("[MISS] Filme não encontrado no Cache Local.");
            System.out.println("[REDE] Preparando requisição ao servidor (Aguardando Huffman)...");
            System.out.println("---------------------------------------------------");
            
            String pedidoMiss = "MISS:" + id;
            PacoteDados pacoteEnvio = rede.comprimirETransmitir(pedidoMiss);
            
            PacoteDados pacoteResposta = servidor.receberRequisicao(pacoteEnvio);
            
            String resposta = ArvoreHuffman.decodificar(pacoteResposta.getBits(), pacoteResposta.getRaizDecodificacao());
            
            processarRespostaMiss(resposta);

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

    private void filtrarEExibirRecomendacao(String recomendacoes, String categoria, boolean isGlobal) {
        if (recomendacoes.equals("REC: VAZIO")) {
            return;
        }

        String stringLimpa = recomendacoes.replace("REC: ", "").trim();
        String[] opcoes = stringLimpa.split(";");

        boolean recomendou = false;
        int limiteTentativas = 3;
        int tentativas = 0;

        if (isGlobal) {
            System.out.println("\n[TENDÊNCIA GLOBAL DA PLATAFORMA]");
            System.out.println("Títulos em alta no gênero " + categoria.toUpperCase() + ":");
        } else {
            System.out.println("\n[RECOMENDAÇÃO CONTEXTUAL]");
            System.out.println("Porque você acabou de buscar por " + categoria.toUpperCase() + ":");
        }

        for (String opcao : opcoes) {
            if (tentativas >= limiteTentativas) break;

            String[] dados = opcao.trim().split(",");
            int idSorteado = Integer.parseInt(dados[0]);
            String nomeSorteado = dados[1];

            if (cacheHash.buscar(idSorteado) == null) {
                System.out.println("> " + nomeSorteado + " (ID: " + idSorteado + ")");
                recomendou = true;
                break;
            }
            tentativas++;
        }

        if (!recomendou) {
            System.out.println("> Você já assistiu aos títulos mais recentes desta tendência!");
        }
    }

    public void processarRespostaMiss(String respostaServidor) {
        if (respostaServidor.equals("ERRO:FILME_NAO_ENCONTRADO")) {
            System.out.println("Erro: O filme solicitado não existe no catálogo.");
            return;
        }

        String[] partes = respostaServidor.split("\\|");
        String dadosFilmeStr = partes[0];
        String generoGlobal = partes[1];
        String recomendacoesGlobais = partes[2];
        String recomendacoesLocais = partes.length > 3 ? partes[3] : "REC: VAZIO";

        String[] atributos = dadosFilmeStr.split(";");
        Filme filme = new Filme(Integer.parseInt(atributos[0]), atributos[1], Integer.parseInt(atributos[2]), 
            atributos[3],  // sinopse
            atributos[4]   // categoria
        );

        System.out.println("[SUCESSO] Filme recebido do servidor!");
        System.out.println(filme);
        
        atualizarCache(filme);

        System.out.println("---------------------------------------------------");
        System.out.println("                 SUGESTÕES DE FILMES               ");
        System.out.println("---------------------------------------------------");

        filtrarEExibirRecomendacao(recomendacoesLocais, filme.getCategoria(), false);

        filtrarEExibirRecomendacao(recomendacoesGlobais, generoGlobal, true);
        
        System.out.println("===================================================");
    }
}