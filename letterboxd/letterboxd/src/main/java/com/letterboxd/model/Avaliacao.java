package com.letterboxd.model;

public class Avaliacao {
    private int id;
    private Usuario usuario;
    private Filme filme;
    private double nota; // Ex: 0.0 a 5.0 estrelas
    private String comentario;

    public Avaliacao(int id, Usuario usuario, Filme filme, double nota, String comentario) {
        this.id = id;
        this.usuario = usuario;
        this.filme = filme;
        this.nota = nota;
        this.comentario = comentario;
    }

    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Filme getFilme() { return filme; }
    public double getNota() { return nota; }
    public String getComentario() { return comentario; }
}