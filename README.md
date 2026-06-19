# Projeto da 3ª Unidade - Estrutura de Dados II

Este repositório contém a implementação final do sistema de streaming, buscas e recomendações de filmes solicitado como projeto para a terceira unidade da disciplina de Estrutura de Dados II.

O projeto evolui a arquitetura Cliente-Servidor anterior, introduzindo um sistema robusto de recomendações, compressão de tráfego de dados e estruturas autoajustáveis para otimizar o acesso às informações mais relevantes.

## Estruturas Utilizadas

* **Cliente (Cache e Preferências):** * O cache local é gerenciado por uma **Lista Autoajustável** utilizando a política de substituição **LRU** (Least Recently Used), em conjunto com uma **Tabela Hash** para garantir buscas em tempo constante $O(1)$. O tamanho do cache é rigorosamente limitado.
  * O histórico e as preferências do usuário são monitorados por uma **Árvore Splay**, garantindo que os filmes recém-acessados fiquem próximos à raiz.

* **Servidor (Banco de Dados e Popularidade):** * O armazenamento físico do catálogo é feito em uma **Lista Ligada** padrão.
  * A indexação primária por ID e a busca por Nome são gerenciadas por **Tabelas Hash** utilizando encadeamento para tratamento de colisões.
  * A popularidade global (filmes mais acessados por todos os clientes) é controlada de forma independente por uma segunda **Árvore Splay**.

* **Rede (Middleware de Transmissão):**
  * A comunicação bidirecional entre Cliente e Servidor é interceptada por um sistema de rede que aplica compressão de dados.
  * O texto é codificado e decodificado utilizando a **Árvore de Huffman**, construída com o suporte de um **MinHeap** e mapeada por uma **Tabela Hash** genérica desenvolvida do zero, reduzindo o volume de bits trafegados.

## Como Executar
O projeto não exige configurações complexas ou dependências externas. Para rodar o sistema:

1. Clone este repositório.
2. Abra o projeto na sua IDE de preferência.
3. Certifique-se de que o arquivo `filmes.csv` está no diretório correto (`resources/filmes.csv`).
4. Compile e execute o arquivo `App.java` (a classe principal do projeto).
5. O sistema será iniciado diretamente no terminal.