package com.letterboxd.controller;
import com.letterboxd.model.*;
import com.letterboxd.repository.BancoDeDados;
import java.util.*;

public class PerfilController {
    private final Usuario usuario;
    private final BancoDeDados banco;
    public PerfilController(Usuario usuario) {
        this.usuario = usuario;
        banco = BancoDeDados.getInstance();
    }
    public Usuario getUsuario() { return usuario; }
    public List<Filme> getFilmes() { return banco.getFilmes(); }
    public Map<Integer, Reacao> getReacoes() { return banco.getReacoes(usuario); }
    public void avaliar(Filme filme, Reacao escolha) {
        Reacao atual = getReacoes().get(filme.getId());
        banco.salvarReacao(usuario, filme, atual == escolha ? null : escolha);
    }
}
