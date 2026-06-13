package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.LeitorCSV;
import servidor.Servidor;
import cliente.Cliente;
import rede.ConexaoRede;
import model.Filme;

public class App {
    public static void main(String[] args) {

        LeitorCSV leitor = new LeitorCSV();
        List<Filme> catalogo = leitor.lerFilmes("resources/filmes.csv");

        ConexaoRede rede = new ConexaoRede();
        Servidor servidor = new Servidor(rede);
    
        for (Filme filme : catalogo) {
            servidor.cadastrarFilme(filme);
        }
        
        List<Cliente> listaClientes = new ArrayList<>();
        listaClientes.add(new Cliente("Gabriel", "senha123", servidor, rede));
        listaClientes.add(new Cliente("Henrique", "senha321", servidor, rede));
        listaClientes.add(new Cliente("Felipe", "senha456", servidor, rede));
        
        for (Cliente cliente : listaClientes) {
            for (int i = 0; i < Math.min(50, catalogo.size()); i++) {
                cliente.atualizarCache(catalogo.get(i));
            }
        }

        Scanner input = new Scanner(System.in);
        boolean rodarSistema = true;

        while (rodarSistema) {
            
            Cliente clienteLogado = realizarLogin(listaClientes, input);
            
            int paginaAtual = 1;
            int totalPaginas = 50;
            boolean sessaoAtiva = true;
            
            while (sessaoAtiva) {
                limparTela();
                System.out.println("=========================================================================");
                System.out.println(" SISTEMA DE STREAMING | UTILIZADOR: " + clienteLogado.getNomeUsuario().toUpperCase());
                System.out.println(" Catálogo Geral - Página " + paginaAtual + "/" + totalPaginas);
                System.out.println("=========================================================================");
                System.out.printf("%-5s | %-50s | %s\n", "ID", "NOME", "GÊNERO");
                System.out.println("-------------------------------------------------------------------------");

                List<Filme> paginaDeFilmes = servidor.obterPagina(paginaAtual, 20);
                for (Filme f : paginaDeFilmes) {
                    System.out.printf("%-5d | %-50s | %s\n", f.getId(), f.getNome(), f.getCategoria());
                }

                System.out.println("=========================================================================");
                System.out.println("OPÇÕES: [P] Próxima | [V] Voltar | [T] Roteiro de Testes | [R] Relatório Huffman");
                System.out.println("        [L] Fazer Logout  | [S] Sair do Sistema");
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("BUSCA:  Digite o NOME exato do filme.");
                System.out.print("\nEscolha uma opção ou faça uma busca: ");

                String entrada = input.nextLine();
                String comandoUpper = entrada.toUpperCase().trim();

                switch (comandoUpper) {
                    case "P":
                        if (paginaAtual < totalPaginas) paginaAtual++;
                        break;
                    case "V":
                        if (paginaAtual > 1) paginaAtual--;
                        break;
                    case "T":
                        limparTela();
                        int[] hits, miss, falhas;
                        String nomeTeste;

                        if (clienteLogado.getNomeUsuario().equalsIgnoreCase("Henrique")) {
                            nomeTeste = "Perfil Ação";
                            hits = new int[]{2, 12, 22};
                            miss = new int[]{522, 542, 562};
                            falhas = new int[]{3001, 3002};
                        } else if (clienteLogado.getNomeUsuario().equalsIgnoreCase("Gabriel")) {
                            nomeTeste = "Perfil Ficção Científica";
                            hits = new int[]{5, 15, 25};
                            miss = new int[]{622, 642, 662};
                            falhas = new int[]{4001, 4002};
                        } else {
                            nomeTeste = "Perfil Comédia";
                            hits = new int[]{8, 18, 28};
                            miss = new int[]{722, 742, 762};
                            falhas = new int[]{5001, 5002};
                        }

                        rodarTestePersonalizado(clienteLogado, servidor, nomeTeste, hits, miss, falhas);
                        pausar(input);
                        break;

                    case "L":
                        System.out.println("\nEncerrando a sessão de " + clienteLogado.getNomeUsuario() + "...");
                        sessaoAtiva = false; 
                        pausar(input);
                        break;

                    case "S":
                        System.out.println("\nEncerrando o sistema global... Até logo!");
                        sessaoAtiva = false;
                        rodarSistema = false;
                        break;
                    
                    case "R":
                        limparTela();
                        rede.exibirRelatorioCompressao();
                        pausar(input);
                        break;

                    default:
            
                        int idEncontrado = servidor.buscarIdPorNome(entrada);
                        limparTela();
                        if (idEncontrado != -1) {
                            clienteLogado.solicitarFilme(idEncontrado);
                        } else {
                            System.out.println("[AVISO] O termo \"" + entrada + "\" não corresponde a um ID válido ou nome cadastrado.");
                        }
                        pausar(input);
                        break;
                }
            }
        }

        input.close();
    }   

    private static Cliente realizarLogin(List<Cliente> listaClientes, Scanner teclado) {
        while (true) {
            limparTela();
            System.out.println("===================================================");
            System.out.println("            SISTEMA DE STREAMING - LOGIN           ");
            System.out.println("===================================================");
            System.out.print("Usuário: ");
            String login = teclado.nextLine();
            System.out.print("Senha: ");
            String senha = teclado.nextLine();

            for (Cliente cliente : listaClientes) {
                if (cliente.getNomeUsuario().equals(login) && cliente.getSenha().equals(senha)) {
                    System.out.println("\n[SUCESSO] Acesso concedido! Bem-vindo, " + cliente.getNomeUsuario() + ".");
                    pausar(teclado);
                    return cliente; 
                }
            }

            System.out.println("\n[ERRO] Credenciais inválidas. Tente novamente.");
            pausar(teclado);
        }
    }

  public static void rodarTestePersonalizado(Cliente clienteLogado, Servidor servidor, String nomeTeste, int[] hits, int[] miss, int[] falhas) {
        System.out.println("\n\n\n>>> INICIANDO BATERIA DE TESTES: " + nomeTeste.toUpperCase() + " <<<");
        System.out.println("Usuário em teste: " + clienteLogado.getNomeUsuario() + "\n");

        System.out.println("--- TESTE 1: BUSCAS COM SUCESSO NO CACHE (HITS) ---");
        for (int id : hits) {
            clienteLogado.solicitarFilme(id);
        }

        System.out.println("\n\n\n--- TESTE 2: BUSCAS NO SERVIDOR (MISS NO CACHE) ---");
        for (int id : miss) {
            clienteLogado.solicitarFilme(id);
        }

        System.out.println("\n\n\n--- TESTE 3: BUSCAS INVÁLIDAS (FALHAS) ---");
        for (int id : falhas) {
            clienteLogado.solicitarFilme(id);
        }

        System.out.println("\n\n\n>>> ROTEIRO DE BUSCAS FINALIZADO <<<");
        System.out.println("Gerando relatórios de balanceamento das estruturas...\n");

        clienteLogado.getPreferencias().exibirPreferenciasCliente();

        servidor.exibirPopularidadeGlobal();
        
        System.out.println("\n>>> FIM DO TESTE <<<");
    }

    private static void limparTela() {
        try {
            String sistemaOperacional = System.getProperty("os.name").toLowerCase();
            
            if (sistemaOperacional.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar a tela: " + e.getMessage());
        }
    }

    private static void pausar(Scanner teclado) {
        System.out.println("\nPressione [ENTER] para continuar...");
        teclado.nextLine();
    }
}