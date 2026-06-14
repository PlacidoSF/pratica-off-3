package servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import estruturas.listaLigada.*;
import estruturas.hash.*;
import estruturas.hashMap.*;
import estruturas.arvoreSplay.ArvoreSplay;
import model.Filme;
import rede.ConexaoRede;
import rede.PacoteDados;
import estruturas.huffman.ArvoreHuffman;

public class Servidor {
    private ListaLigada dados;
    private TabelaHash<NoLista> index;
    private ArvoreSplay popularidadeGlobal;
    private HashMap<String, Integer> dicionarioNomes;
    private ConexaoRede rede;

    public Servidor(ConexaoRede rede) {
        this.dados = new ListaLigada();
        this.index = new TabelaHash<>(173);
        this.popularidadeGlobal = new ArvoreSplay();
        this.dicionarioNomes = new HashMap<>(173);
        this.rede = rede;
    }

    public void cadastrarFilme(Filme filmeValor) {
        NoLista novoNo = this.dados.inserir(filmeValor);
        this.index.inserir(filmeValor.getId(), novoNo);

        this.dicionarioNomes.put(filmeValor.getNome().toUpperCase(), filmeValor.getId());
    }

    public int buscarIdPorNome(String nome) {
        Integer id = this.dicionarioNomes.get(nome.toUpperCase());
        
        return (id != null) ? id : -1; 
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

    public String recomendarPorGenero(String genero) {
        List<Filme> filmesDoGenero = new ArrayList<>();
        NoLista atual = this.dados.getList();

        while (atual != null) {
            Filme filme = atual.getValorFilme();
            if (filme.getCategoria().equalsIgnoreCase(genero)) {
                filmesDoGenero.add(filme);
            }
            atual = atual.getProxNo();
        }

        if (filmesDoGenero.isEmpty()) {
            return "REC: VAZIO";
        }

        Random random = new Random();
        StringBuilder recomendacao = new StringBuilder("REC:");
        

        int limite = 5;

        for (int i = 0; i < limite; i++) {
            int indexSorteado = random.nextInt(filmesDoGenero.size());
            Filme sorteado = filmesDoGenero.remove(indexSorteado);


            recomendacao.append(" ").append(sorteado.getId()).append(",")
                        .append(sorteado.getNome()).append(",")
                        .append(sorteado.getCategoria());

            if (i < limite - 1) {
                recomendacao.append(";");
            }
        }

        return recomendacao.toString();
    }

    public String atenderMiss(int id) {

        String generoGlobalAntigo = null;
        if (this.popularidadeGlobal.getRaiz() != null) {
            generoGlobalAntigo = this.popularidadeGlobal.getRaiz().getFilme().getCategoria();
        }

        Filme filmeEncontrado = buscarComIndice(id);
        Filme filmeEncontradoSemIndice = buscarSemIndice(id);
        
        if (filmeEncontrado == null) {
            return "ERRO:FILME_NAO_ENCONTRADO";
        }

        if (generoGlobalAntigo == null) {
            generoGlobalAntigo = filmeEncontrado.getCategoria();
        }

        String recomendacaoGlobal = recomendarPorGenero(generoGlobalAntigo);

        String recomendacaoLocal = recomendarPorGenero(filmeEncontrado.getCategoria());

        String dadosFilme = filmeEncontrado.getId() + ";" + 
                            filmeEncontrado.getNome() + ";" + 
                            filmeEncontrado.getAno() + ";" +
                            filmeEncontrado.getSinopse() + ";" + 
                            filmeEncontrado.getCategoria();

        // filme | gêneroGlobal | recomendaçãoGlobal | recomendaçãoLocal
        return dadosFilme + "|" + generoGlobalAntigo + "|" + recomendacaoGlobal + "|" + recomendacaoLocal;
    }

    public PacoteDados receberRequisicao(PacoteDados pacoteEntrada) {

        String requisicaoTexto = ArvoreHuffman.decodificar(pacoteEntrada.getBits(), pacoteEntrada.getRaizDecodificacao());
        
        String respostaBruta = "";

        String[] partes = requisicaoTexto.split(":");
        String comando = partes[0];
        String parametro = partes[1];

        if (comando.equals("MISS")) {
            int id = Integer.parseInt(parametro);
            respostaBruta = atenderMiss(id);
        } else if (comando.equals("REC")) {
            respostaBruta = recomendarPorGenero(parametro);
        }

        return rede.comprimirETransmitir(respostaBruta);
    }
}