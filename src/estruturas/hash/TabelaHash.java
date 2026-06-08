package estruturas.hash;

import estruturas.listaLigada.NoLista;

public class TabelaHash {
    private static final int tamanhoT = 173;
    private NoHash[] tabela = new NoHash[tamanhoT];
    private int comparacoesBusca;

    public TabelaHash() {}

    public int getComparacoesBusca() {
        return comparacoesBusca;
    }

    public void setComparacoesBusca(int comparacoesBusca) {
        this.comparacoesBusca = comparacoesBusca;
    }

    public int hash(int chave) {
        return (chave & 0x7FFFFFFF) % tamanhoT;
    }

    //final da lista
    public void inserir(int chave, NoLista referenciaLista) {
        int posicao = hash(chave);

        NoHash noAtual = tabela[posicao];
        NoHash noAnterior = null;

        if (noAtual == null) {
            tabela[posicao] = new NoHash(chave, referenciaLista);
        } 

        else {
            while (noAtual != null) {
                if (noAtual.getChave() == chave) {
                    break;
                }
                noAnterior = noAtual;
                noAtual = noAtual.getProxNo();
            }

            if (noAtual == null) {
                noAnterior.setProxNo(new NoHash(chave, referenciaLista));
            } 
        }
    }

    public NoLista buscar(int chave) {
        this.comparacoesBusca = 0;
        int posicao = hash(chave);
        NoHash noAtual = tabela[posicao];

        while (noAtual != null) {
            this.comparacoesBusca++;
            if (noAtual.getChave() == chave) {
                return noAtual.getReferenciaLista();
            }
            noAtual = noAtual.getProxNo();
        }
        return null;
    }
}
