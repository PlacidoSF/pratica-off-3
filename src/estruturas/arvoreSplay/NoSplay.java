package estruturas.arvoreSplay;

import model.Filme;

public class NoSplay {
    int chave; 
    Filme filme;
    int contadorAcessos;
    NoSplay esq;
    NoSplay dir;

    public NoSplay(Filme filme) {
        this.chave = filme.getId();
        this.filme = filme;
        this.contadorAcessos = 0;
        this.esq = null;
        this.dir = null;
    }

    public void incrementarAcesso() {
        this.contadorAcessos++;
    }

    public int getChave() {
        return chave;
    }

    public Filme getFilme() {
        return filme;
    }

    public int getContadorAcessos() {
        return contadorAcessos;
    }
}