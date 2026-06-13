package estruturas.huffman;

import estruturas.hashMap.*;
import java.util.List; 
import estruturas.minHeap.MinHeap;

public class ArvoreHuffman {

    public static HashMap<String, Integer> calcularFrequencias(String texto) {

        HashMap<String, Integer> freq = new HashMap<>(251);

        for (char c : texto.toCharArray()) {
            String chave = String.valueOf(c); 
            
            if (freq.containsKey(chave)) {
                freq.put(chave, freq.get(chave) + 1);
            } else {
                freq.put(chave, 1);
            }
        }
        return freq;
    }

    public static NoHuffman construirArvoreHuffman(String texto) {
        HashMap<String, Integer> frequencias = calcularFrequencias(texto);
        MinHeap heap = new MinHeap(frequencias.size());

        List<NoHashMap<String, Integer>> entradas = frequencias.obterEntradas();
        
        for (NoHashMap<String, Integer> entry : entradas) {
            heap.inserir(new NoHuffman(entry.getChave(), entry.getValor()));
        }

        if (heap.tamanho() == 1) {
            NoHuffman unico = heap.removerMin();
            return new NoHuffman(unico.frequencia, unico, null);
        }

        while (heap.tamanho() > 1) {
            NoHuffman x = heap.removerMin();
            NoHuffman y = heap.removerMin();
            NoHuffman z = new NoHuffman(x.frequencia + y.frequencia, x, y);
            heap.inserir(z);
        }

        return heap.removerMin();
    }

    public static void gerarCodigos(NoHuffman no, String codigo, HashMap<String, String> tabela) {
        if (no == null) {
            return;
        }

        if (no.isFolha()) {
            tabela.put(no.caractere, codigo);
        }

        gerarCodigos(no.esq, codigo + "0", tabela);
        gerarCodigos(no.dir, codigo + "1", tabela);
    }

    public static String codificar(String texto, HashMap<String, String> tabela) {
        StringBuilder codificada = new StringBuilder();
        
        for (char c : texto.toCharArray()) {
            String chave = String.valueOf(c);
            codificada.append(tabela.get(chave));
        }
        
        return codificada.toString();
    }

    public static String decodificar(String codificado, NoHuffman raiz) {
        StringBuilder decodificada = new StringBuilder();
        NoHuffman atual = raiz;

        for (char bit : codificado.toCharArray()) {
            atual = (bit == '0') ? atual.esq : atual.dir;

            if (atual.isFolha()) {
                decodificada.append(atual.caractere);
                atual = raiz; 
            }
        }
        
        return decodificada.toString();
    }
}