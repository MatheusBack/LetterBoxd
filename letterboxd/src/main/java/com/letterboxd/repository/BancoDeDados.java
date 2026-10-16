package com.letterboxd.repository;

import com.letterboxd.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BancoDeDados {
    private static BancoDeDados instance;

    private List<Usuario> usuarios = new ArrayList<>();
    private List<Genero> generos = new ArrayList<>();
    private List<Filme> filmes = new ArrayList<>();
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    private BancoDeDados() {
        inicializarDados();
    }

    public static synchronized BancoDeDados getInstance() {
        if (instance == null) {
            instance = new BancoDeDados();
        }
        return instance;
    }

    private void inicializarDados() {
        // Gêneros (Tabela 1)
        Genero animacao = new Genero(1, "Animação");
        Genero drama = new Genero(2, "Drama");
        Genero suspense = new Genero(3, "Suspense / Psicológico");
        generos.add(animacao);
        generos.add(drama);
        generos.add(suspense);

        // Filmes (Tabela 2)
        Filme f1 = new Filme(1, "Tá Dando Onda", 2007, animacao);
        Filme f2 = new Filme(2, "Perfect Blue", 1997, suspense);
        Filme f3 = new Filme(3, "O Brutalista", 2024, drama);
        filmes.add(f1);
        filmes.add(f2);
        filmes.add(f3);

        // Usuários (Tabela 3)
        Usuario u1 = new Usuario(1, "user", "1234", "Cinéfilo Criativo");
        usuarios.add(u1);

        // Avaliações (Tabela 4)
        avaliacoes.add(new Avaliacao(1, u1, f1, 4.5, "Incrível clássico da animação sobre surfe!"));
        avaliacoes.add(new Avaliacao(2, u1, f2, 5.0, "Obra-prima do suspense psicológico de Satoshi Kon."));
        avaliacoes.add(new Avaliacao(3, u1, f3, 4.0, "Atuações impecáveis e excelente direção de arte."));
    }

    public Usuario autenticar(String username, String senha) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username) && u.getSenha().equals(senha))
                .findFirst()
                .orElse(null);
    }

    public List<Avaliacao> getAvaliacoesPorUsuario(Usuario usuario) {
        return avaliacoes.stream()
                .filter(a -> a.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
    }
}