package estruturas.huffman;

public class NoHuffman {
    public String caractere; 
    public int frequencia;
    public NoHuffman esq, dir;

    public NoHuffman(String caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.esq = null;
        this.dir = null;
    }

    public NoHuffman(int frequencia, NoHuffman esq, NoHuffman dir) {
        this.caractere = null; 
        this.frequencia = frequencia;
        this.esq = esq;
        this.dir = dir;
    }

    public boolean isFolha() {
        return esq == null && dir == null;
    }
}