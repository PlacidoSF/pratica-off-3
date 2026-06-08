package estruturas.hash;

public class TabelaHash<T> {
    private int tamanho;
    private NoHash<T>[] tabela;
    private int comparacoesBusca;

    public TabelaHash(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = (NoHash<T>[]) new NoHash[tamanho];
    }

    public int getComparacoesBusca() {
        return comparacoesBusca;
    }

    public void setComparacoesBusca(int comparacoesBusca) {
        this.comparacoesBusca = comparacoesBusca;
    }

    public int hash(int chave) {
        return (chave & 0x7FFFFFFF) % tamanho;
    }

    // final da lista
    public void inserir(int chave, T referencia) {
        int posicao = hash(chave);

        NoHash<T> noAtual = tabela[posicao];
        NoHash<T> noAnterior = null;

        if (noAtual == null) {
            tabela[posicao] = new NoHash<>(chave, referencia);
        } else {
            while (noAtual != null) {
                if (noAtual.getChave() == chave) {
                    break;
                }
                noAnterior = noAtual;
                noAtual = noAtual.getProxNo();
            }

            if (noAtual == null) {
                noAnterior.setProxNo(new NoHash<>(chave, referencia));
            } 
        }
    }

    public T buscar(int chave) {
        this.comparacoesBusca = 0;
        int posicao = hash(chave);
        NoHash<T> noAtual = tabela[posicao];

        while (noAtual != null) {
            this.comparacoesBusca++;
            if (noAtual.getChave() == chave) {
                return noAtual.getReferencia();
            }
            noAtual = noAtual.getProxNo();
        }
        return null;
    }

    // remoção: LRU
    public void remover(int chave) {
        int posicao = hash(chave);
        NoHash<T> noAtual = tabela[posicao];
        NoHash<T> noAnterior = null;

        while (noAtual != null) {
            if (noAtual.getChave() == chave) {
                
                // primeiro do array
                if (noAnterior == null) {
                    tabela[posicao] = noAtual.getProxNo();
                } 
                // meio ou final
                else {
                    noAnterior.setProxNo(noAtual.getProxNo());
                }
                return; 
            }
            noAnterior = noAtual;
            noAtual = noAtual.getProxNo();
        }
    }
}