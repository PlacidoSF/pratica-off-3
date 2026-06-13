package rede;

import estruturas.huffman.NoHuffman;

public class PacoteDados {
    private String bits;
    private NoHuffman raizDecodificacao;

    public PacoteDados(String bits, NoHuffman raizDecodificacao) {
        this.bits = bits;
        this.raizDecodificacao = raizDecodificacao;
    }

    public String getBits() {
        return bits;
    }

    public NoHuffman getRaizDecodificacao() {
        return raizDecodificacao;
    }
}