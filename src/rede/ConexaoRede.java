package rede;

import estruturas.huffman.ArvoreHuffman;
import estruturas.huffman.NoHuffman;
import estruturas.hashMap.HashMap;
import estruturas.hashMap.NoHashMap;

public class ConexaoRede {
    
    private String textoOriginal;
    private String textoComprimido;
    private NoHuffman raizAtual;
    private HashMap<String, String> tabelaAtual;
    private int bitsOriginal;
    private int bitsComprimido;

    public ConexaoRede() {
    }

    public PacoteDados comprimirETransmitir(String mensagemBruta) {

        this.textoOriginal = mensagemBruta;
        this.bitsOriginal = mensagemBruta.length() * 8; 

        this.raizAtual = ArvoreHuffman.construirArvoreHuffman(mensagemBruta);

        this.tabelaAtual = new HashMap<>(251);
        ArvoreHuffman.gerarCodigos(this.raizAtual, "", this.tabelaAtual);

        this.textoComprimido = ArvoreHuffman.codificar(mensagemBruta, this.tabelaAtual);
        this.bitsComprimido = this.textoComprimido.length();

        return new PacoteDados(this.textoComprimido, this.raizAtual);
    }

    public void exibirRelatorioCompressao() {
        if (textoOriginal == null) {
            System.out.println("Nenhuma transmissão foi realizada ainda.");
            return;
        }


        double taxa = (1.0 - ((double) bitsComprimido / bitsOriginal)) * 100;

        System.out.println("\n===================================================");
        System.out.println("              RELATÓRIO DE REDE (HUFFMAN)          ");
        System.out.println("===================================================");
        System.out.println("Texto Original Bruto: \n" + textoOriginal);
        System.out.println("---------------------------------------------------");
        System.out.println("Texto Comprimido (Bits): \n" + textoComprimido);
        System.out.println("---------------------------------------------------");
        System.out.println("Tamanho Original:   " + bitsOriginal + " bits");
        System.out.println("Tamanho Comprimido: " + bitsComprimido + " bits");
        System.out.printf("Taxa de Compressão: %.2f%%\n", taxa);
        System.out.println("---------------------------------------------------");
        System.out.println("Tabela de Códigos Gerada:");
        
        for (NoHashMap<String, String> entry : tabelaAtual.obterEntradas()) {
            String chaveVisual = entry.getChave().equals("\n") ? "\\n" : entry.getChave();
            System.out.print("['" + chaveVisual + "' -> " + entry.getValor() + "]  \n");
        }
        System.out.println("===================================================\n");
    }
}