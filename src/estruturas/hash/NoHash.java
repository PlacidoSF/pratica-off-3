package estruturas.hash;

import estruturas.listaLigada.NoLista;

public class NoHash {
    private int chave;
    private NoLista referenciaLista;
    private NoHash proxNo;

    public NoHash(int chave, NoLista referenciaLista) {
        this.chave = chave;
        this.referenciaLista = referenciaLista;
        this.proxNo = null;
    }

    public int getChave() {
        return chave;
    }

    public void setChave(int chave) {
        this.chave = chave;
    }

    public NoLista getReferenciaLista() {
        return referenciaLista;
    }

    public void setReferenciaLista(NoLista referenciaLista) {
        this.referenciaLista = referenciaLista;
    }

    public NoHash getProxNo() {
        return proxNo;
    }

    public void setProxNo(NoHash proxNo) {
        this.proxNo = proxNo;
    }
}
