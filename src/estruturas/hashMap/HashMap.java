package estruturas.hashMap;

import java.util.ArrayList;
import java.util.List;

public class HashMap<K, V> {
    private NoHashMap<K, V>[] tabela;
    private int capacidade;
    private int tamanho;

    public HashMap(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new NoHashMap[capacidade];
        this.tamanho = 0;
    }

    private int calcularHash(K chave) {
        return Math.abs(chave.hashCode()) % capacidade;
    }

    public void put(K chave, V valor) {
        int indice = calcularHash(chave);
        NoHashMap<K, V> atual = tabela[indice];

        while (atual != null) {
            if (atual.getChave().equals(chave)) {
                atual.setValor(valor);
                return;
            }
            atual = atual.getProximo();
        }

        NoHashMap<K, V> novoNo = new NoHashMap<>(chave, valor);
        novoNo.setProximo(tabela[indice]);
        tabela[indice] = novoNo;
        tamanho++;
    }

    public V get(K chave) {
        int indice = calcularHash(chave);
        NoHashMap<K, V> atual = tabela[indice];

        while (atual != null) {
            if (atual.getChave().equals(chave)) {
                return atual.getValor();
            }
            atual = atual.getProximo();
        }
        
        return null;
    }

    public boolean containsKey(K chave) {
        return get(chave) != null;
    }

    public int size() {
        return tamanho;
    }

    public List<NoHashMap<K, V>> obterEntradas() {
        List<NoHashMap<K, V>> entradas = new ArrayList<>();
        
        for (int i = 0; i < capacidade; i++) {
            NoHashMap<K, V> atual = tabela[i];
            while (atual != null) {
                entradas.add(atual);
                atual = atual.getProximo();
            }
        }
        
        return entradas;
    }
}