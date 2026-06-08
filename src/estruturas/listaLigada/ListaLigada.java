package estruturas.listaLigada;

import model.Filme;

public class ListaLigada {
    private int comparacoesBusca;
    private NoLista lista = null;

    public ListaLigada() {}

    public int getComparacoesBusca() {
        return comparacoesBusca;
    }

    public NoLista inserir(Filme filmeValor) {
        NoLista novoNo = new NoLista(filmeValor);
        if (lista == null) {
            lista = novoNo;
        } else {
            NoLista noAtual = lista;
            while (noAtual.getProxNo() != null) {
                noAtual = noAtual.getProxNo();
            }
            noAtual.setProxNo(novoNo);
        }
        return novoNo;
    }

    public NoLista buscar(int id) {
        this.comparacoesBusca = 0;
        NoLista noAtual = lista;
        while (noAtual != null) {
            this.comparacoesBusca++;
            if (noAtual.getValorFilme().getId() == id) {
                return noAtual;
            }
            noAtual = noAtual.getProxNo();
        }
        return null;
    }

    public NoLista getList() {
        return this.lista;
    }
}
