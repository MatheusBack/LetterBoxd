package com.letterboxd.model;

public class Filme {
    private int id;
    private String titulo;
    private int anoLancamento;
    private Genero genero;

    public Filme(int id, String titulo, int anoLancamento, Genero genero) {
        this.id = id;
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
    }

    public String getPoster() { return "/posters/" + (id == 1 ? "onda.jpg" : id == 2 ? "perfect.jpg" : "brutalista.jpg"); }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAnoLancamento() { return anoLancamento; }
    public Genero getGenero() { return genero; }
}