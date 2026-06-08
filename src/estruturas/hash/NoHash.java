package estruturas.hash;

public class NoHash<T> {
    private int chave;
    private T referencia;
    private NoHash<T> proxNo;

    public NoHash(int chave, T referencia) {
        this.chave = chave;
        this.referencia = referencia;
        this.proxNo = null;
    }

    public int getChave() {
        return chave;
    }

    public void setChave(int chave) {
        this.chave = chave;
    }

    public T getReferencia() {
        return referencia;
    }

    public void setReferencia(T referencia) {
        this.referencia = referencia;
    }

    public NoHash<T> getProxNo() {
        return proxNo;
    }

    public void setProxNo(NoHash<T> proxNo) {
        this.proxNo = proxNo;
    }
}