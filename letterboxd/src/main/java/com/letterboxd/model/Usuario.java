package com.letterboxd.model;

public class Usuario {
    private int id;
    private String username;
    private String senha;
    private String nomeExibicao;

    public Usuario(int id, String username, String senha, String nomeExibicao) {
        this.id = id;
        this.username = username;
        this.senha = senha;
        this.nomeExibicao = nomeExibicao;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getSenha() { return senha; }
    public String getNomeExibicao() { return nomeExibicao; }
}