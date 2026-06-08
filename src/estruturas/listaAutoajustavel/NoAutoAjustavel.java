package estruturas.listaAutoajustavel;

import model.Filme;

public class NoAutoAjustavel {
    
    private Filme filme;
    private NoAutoAjustavel proximo;

    public NoAutoAjustavel(Filme filme) {
        this.filme = filme;
        this.proximo = null;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public NoAutoAjustavel getProximo() {
        return proximo;
    }

    public void setProximo(NoAutoAjustavel proximo) {
        this.proximo = proximo;
    }
}