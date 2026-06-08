package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import model.Filme;

public class LeitorCSV {
    public List<Filme> lerFilmes(String caminhoArquivo) {
        List<Filme> filmes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File(caminhoArquivo)))) {

            String linha = br.readLine();
            linha = br.readLine();

            while (linha != null) {
                String[] itens = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                int id = Integer.parseInt(itens[0].replace("\"", "").trim());
                String nome = itens[1].replace("\"", "").trim();
                int ano = Integer.parseInt(itens[2].replace("\"", "").trim());
                String sinopse = itens[3].replace("\"", "").trim();
                String genero = itens[4].replace("\"", "").trim();

                filmes.add(new Filme(id, nome, ano, sinopse, genero));
                linha = br.readLine();
            }

        return filmes;

        } catch (Exception e) {
           System.out.println("error: " + e.getMessage());
            return null;
        }
    }
}
