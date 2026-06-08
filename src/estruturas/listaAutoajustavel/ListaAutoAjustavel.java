package estruturas.listaAutoajustavel;

import model.Filme;

public class ListaAutoAjustavel {

    private NoAutoAjustavel primeiro = null;

    public NoAutoAjustavel getPrimeiro() {
        return primeiro;
    }

    public void inserir(Filme filme) {
        NoAutoAjustavel novo = new NoAutoAjustavel(filme);

        if (primeiro != null) {
            novo.setProximo(primeiro);
        }
        
        primeiro = novo;
    }

    public Filme buscarMF(int id) {
        NoAutoAjustavel atual;
        NoAutoAjustavel anterior = null;

        for (atual = primeiro; atual != null; atual = atual.getProximo()) {

            if (atual.getFilme().getId() == id) {

                if (atual != primeiro) {

                    anterior.setProximo(atual.getProximo());


                    atual.setProximo(primeiro);
                    primeiro = atual;
                }

                return atual.getFilme();
            }

            anterior = atual;
        }

        return null;
    }

    // remoção do último
    public Filme removerUltimo() {
        if (primeiro == null) {
            return null;
        }

        if (primeiro.getProximo() == null) {
            Filme removido = primeiro.getFilme();
            primeiro = null;
            return removido;
        }

        NoAutoAjustavel atual = primeiro;
        NoAutoAjustavel anterior = null;

        while (atual.getProximo() != null) {
            anterior = atual;
            atual = atual.getProximo();
        }

        anterior.setProximo(null);
        return atual.getFilme();
    }

    // 10 filmes mais utilizados
    public void imprimirMaisRecentes() {
        NoAutoAjustavel atual = primeiro;
        int cont = 0;

        System.out.println("--- 10 Filmes Mais Recentemente Utilizados ---");
        
        if (primeiro == null) {
            System.out.println("O cache está vazio.");
            return;
        }

        while (atual != null && cont < 10) {
            System.out.println((cont + 1) + ". ID: " + atual.getFilme().getId() + " | " + atual.getFilme().getNome());
            atual = atual.getProximo();
            cont++;
        }
        System.out.println("----------------------------------------------");
    }
}