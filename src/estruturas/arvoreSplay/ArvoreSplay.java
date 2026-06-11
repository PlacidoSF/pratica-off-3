package estruturas.arvoreSplay;

import model.Filme;
import java.util.LinkedList;
import java.util.Queue;

public class ArvoreSplay {

    private NoSplay raiz;

    public ArvoreSplay() {
        raiz = null;
    }

    public NoSplay getRaiz() {
        return raiz;
    }

    private NoSplay rds(NoSplay x) {
        NoSplay y = x.esq;
        x.esq = y.dir;
        y.dir = x;
        return y;
    }

    private NoSplay res(NoSplay x) {
        NoSplay y = x.dir;
        x.dir = y.esq;
        y.esq = x;
        return y;
    }

    public Filme buscar(int chave) {
        if (raiz == null) {
            return null;
        }

        raiz = difusao(raiz, chave);

        if (raiz.chave == chave) {
            raiz.incrementarAcesso(); 
            return raiz.filme;
        }
        return null;
    }


    public void inserir(Filme filme) {
        raiz = inserir(raiz, filme);
    }

    private NoSplay inserir(NoSplay r, Filme filme) {
        int k = filme.getId();
        
        if (r == null) {
            NoSplay novo = new NoSplay(filme);
            novo.incrementarAcesso();
            return novo;
        }

        r = difusao(r, k);

        if (r.chave == k) {
            r.incrementarAcesso();
            return r;
        }

        NoSplay no = new NoSplay(filme);
        no.incrementarAcesso();

        if (r.chave > k) {
            no.dir = r;
            no.esq = r.esq;
            r.esq = null;
        } else {
            no.esq = r;
            no.dir = r.dir;
            r.dir = null;
        }

        return no;
    }

    private NoSplay difusao(NoSplay r, int k) {
        if (r == null || r.chave == k) {
            return r;
        }

        if (r.chave > k) {
            if (r.esq == null) {
                return r;
            }

            if (r.esq.chave > k) {
                r.esq.esq = difusao(r.esq.esq, k);
                r = rds(r);
            } else if (r.esq.chave < k) {
                r.esq.dir = difusao(r.esq.dir, k);
                if (r.esq.dir != null) {
                    r.esq = res(r.esq);
                }
            }

            return (r.esq == null) ? r : rds(r);
        } else {
            if (r.dir == null) {
                return r;
            }

            if (r.dir.chave > k) {
                r.dir.esq = difusao(r.dir.esq, k);
                if (r.dir.esq != null) {
                    r.dir = rds(r.dir);
                }
            } else if (r.dir.chave < k) {
                r.dir.dir = difusao(r.dir.dir, k);
                r = res(r);
            }

            return (r.dir == null) ? r : res(r);
        }
    }

    public void exibirMaisPopulares() {
        if (raiz == null) {
            System.out.println("Nenhum conteúdo foi acessado globalmente até o momento.");
            return;
        }

        Queue<NoSplay> fila = new LinkedList<>();
        fila.add(raiz);
        int cont = 0;

        System.out.println("============================================================================");
        System.out.println("       CONTEÚDOS MAIS POPULARES GLOBALMENTE (ÁRVORE SPLAY)     ");
        System.out.println("============================================================================");
        System.out.printf("%-5s | %-35s | %-20s | %s\n", "POS", "NOME", "CATEGORIA", "ACESSOS");
        System.out.println("----------------------------------------------------------------------------");

        // 10 filmes
        while (!fila.isEmpty() && cont < 10) {
            NoSplay atual = fila.remove();
            cont++;

            System.out.printf("%-5d | %-35s | %-20s | %d\n", 
                              cont, 
                              atual.filme.getNome(), 
                              atual.filme.getCategoria(), 
                              atual.contadorAcessos);

            if (atual.esq != null) fila.add(atual.esq);
            if (atual.dir != null) fila.add(atual.dir);
        }
       System.out.println("============================================================================");
    }

    public void exibirPreferenciasCliente() {
        if (raiz == null) {
            System.out.println("Nenhum histórico de consumo registrado para este usuário.");
            return;
        }

        Queue<NoSplay> fila = new LinkedList<>();
        fila.add(raiz);
        int cont = 0;

        System.out.println("===============================================================");
        System.out.println("          HISTÓRICO RECENTE / PREFERÊNCIAS DO CLIENTE          ");
        System.out.println("===============================================================");
        System.out.printf("%-5s | %-45s | %s\n", "POS", "NOME DO FILME", "ACESSOS");
        System.out.println("---------------------------------------------------------------");

        // 5 filmes
        while (!fila.isEmpty() && cont < 5) {
            NoSplay atual = fila.remove();
            cont++;

            System.out.printf("%-5d | %-45s | %d\n", 
                              cont, 
                              atual.filme.getNome(), 
                              atual.contadorAcessos);

            if (atual.esq != null) fila.add(atual.esq);
            if (atual.dir != null) fila.add(atual.dir);
        }
        System.out.println("===============================================================");
    }
}