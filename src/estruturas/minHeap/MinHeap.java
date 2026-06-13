package estruturas.minHeap;
import estruturas.huffman.NoHuffman;

public class MinHeap {

    private NoHuffman[] heap;
    private int capacidade;
    private int tamanhoHeap;

    public MinHeap(int capacidade) {
        this.capacidade = capacidade;
        this.heap = new NoHuffman[capacidade];
        this.tamanhoHeap = 0;
    }

    public int tamanho() {
        return tamanhoHeap;
    }

    private int piso(double n) {
        return (int) n;
    }

    private void subir(int i) {
        NoHuffman temp;
        int j = piso((i - 1) / 2);

        if (j >= 0 && heap[i].frequencia < heap[j].frequencia) {
            
            temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;

            subir(j);
        }
    }

    private void descer(int i) {
        int j = 2 * i + 1; 
        NoHuffman temp;

        if (j < tamanhoHeap) {
            
            if (j < tamanhoHeap - 1 && heap[j].frequencia > heap[j + 1].frequencia) {
                j++;
            }

            if (heap[j].frequencia < heap[i].frequencia) {
                
                temp = heap[i];
                heap[i] = heap[j];
                heap[j] = temp;
                
                descer(j);
            }
        }
    }

    public void inserir(NoHuffman novo) {
        if (tamanhoHeap == capacidade) {
            System.out.println("Heap cheio. Não é possível inserir!");
            return;
        }

        heap[tamanhoHeap] = novo;
        
        subir(tamanhoHeap);
        tamanhoHeap++;
    }

    public NoHuffman removerMin() {
        if (tamanhoHeap == 0) {
            return null;
        }

        NoHuffman menor = heap[0];

        heap[0] = heap[tamanhoHeap - 1];
        heap[tamanhoHeap - 1] = null;
        tamanhoHeap--;

        descer(0);

        return menor;
    }
    
    public void construir() {
        for (int i = piso((tamanhoHeap - 1) / 2); i >= 0; i--) {
            descer(i);
        }
    }
}