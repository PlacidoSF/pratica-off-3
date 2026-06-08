package model;

public class Filme {
    private int id;
    private String nome;
    private int ano;
    private String sinopse;
    private String categoria;

    public Filme() {    
    }

    public Filme(int id, String nome, int ano, String sinopse, String categoria) {
        setId(id);
        setNome(nome);
        setAno(ano);
        setSinopse(sinopse);
        setCategoria(categoria);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getAno() {
        return ano;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public void setNome(String nome) {
        if (nome != null) {
            this.nome = nome;
        }
    }

    public void setAno(int ano) {
        if (ano > 0) {
            this.ano = ano;
        }
    }

    public void setSinopse(String sinopse) {
        if (sinopse != null) {
            this.sinopse = sinopse;
        }
    }

    public void setCategoria(String categoria) {
        if (categoria != null) {
            this.categoria = categoria;
        }
    }

    @Override
    public String toString() {
        return "Id: " + id + "\n" +
               "Filme: " + nome + "\n" +
               "Ano de Lançamento: " + ano + "\n" +
               "Sinopse: " + sinopse + "\n" +
               "Categoria: " + categoria;
    }
}
