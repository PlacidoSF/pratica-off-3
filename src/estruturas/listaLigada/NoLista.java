package estruturas.listaLigada;

import model.Filme;
public class NoLista {
    private Filme valorFilme;
    private NoLista proxNo;

    public NoLista(Filme valorFilme) {
        this.valorFilme = valorFilme;
        this.proxNo = null;
    }

    public Filme getValorFilme() {
        return valorFilme;
    }

    public void setValorFilme(Filme valorFilme) {
        this.valorFilme = valorFilme;
    }

    public NoLista getProxNo() {
        return proxNo;
    }

    public void setProxNo(NoLista proxNo) {
        this.proxNo = proxNo;
    }

    
}
