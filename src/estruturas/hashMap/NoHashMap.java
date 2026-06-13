package estruturas.hashMap;

public class NoHashMap<K, V> {
    private K chave;
    private V valor;
    private NoHashMap<K, V> proximo;

    public NoHashMap(K chave, V valor) {
        this.chave = chave;
        this.valor = valor;
        this.proximo = null;
    }

    public K getChave() {
        return chave;
    }

    public V getValor() {
        return valor;
    }

    public void setValor(V valor) {
        this.valor = valor;
    }

    public NoHashMap<K, V> getProximo() {
        return proximo;
    }

    public void setProximo(NoHashMap<K, V> proximo) {
        this.proximo = proximo;
    }
}