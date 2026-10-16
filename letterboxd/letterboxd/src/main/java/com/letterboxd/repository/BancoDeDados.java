package com.letterboxd.repository;

import com.letterboxd.model.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class BancoDeDados {
    private static BancoDeDados instance;
    private final String url;

    private BancoDeDados() {
        try {
            Path arquivo = Paths.get(System.getProperty("letterboxd.db",
                    Paths.get(System.getProperty("user.home"), ".letterboxd", "letterboxd.db").toString()));
            if (arquivo.toAbsolutePath().getParent() != null)
                Files.createDirectories(arquivo.toAbsolutePath().getParent());
            url = "jdbc:sqlite:" + arquivo.toAbsolutePath();
            try (Connection c = conectar(); Statement s = c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY, username TEXT UNIQUE NOT NULL, senha TEXT NOT NULL, nome TEXT NOT NULL)");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS generos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL)");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS filmes (id INTEGER PRIMARY KEY, titulo TEXT NOT NULL, ano INTEGER NOT NULL, genero_id INTEGER REFERENCES generos(id))");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS avaliacoes (id INTEGER PRIMARY KEY, usuario_id INTEGER NOT NULL REFERENCES usuarios(id), filme_id INTEGER NOT NULL REFERENCES filmes(id), reacao TEXT NOT NULL CHECK(reacao IN ('LIKE','DISLIKE')), UNIQUE(usuario_id, filme_id))");
                s.executeUpdate("INSERT OR IGNORE INTO usuarios VALUES (1,'user','1234','Cinéfilo Criativo'), (2,'user2','1234','Outro Cinéfilo')");
                s.executeUpdate("INSERT OR IGNORE INTO generos VALUES (1,'Animação'),(2,'Drama'),(3,'Suspense / Psicológico')");
                s.executeUpdate("INSERT OR IGNORE INTO filmes VALUES (1,'Tá Dando Onda',2007,1),(2,'Perfect Blue',1997,3),(3,'O Brutalista',2024,2)");
            }
        } catch (Exception e) { throw new IllegalStateException("Não foi possível abrir o banco de dados.", e); }
    }

    public static synchronized BancoDeDados getInstance() {
        if (instance == null) instance = new BancoDeDados();
        return instance;
    }

    private Connection conectar() throws SQLException {
        Connection c = DriverManager.getConnection(url);
        try (Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys=ON");
            s.execute("PRAGMA busy_timeout=5000");
        }
        return c;
    }

    public Usuario autenticar(String username, String senha) {
        try (Connection c = conectar(); PreparedStatement p = c.prepareStatement(
                "SELECT * FROM usuarios WHERE username=? AND senha=?")) {
            p.setString(1, username); p.setString(2, senha);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new Usuario(r.getInt("id"), r.getString("username"), r.getString("senha"), r.getString("nome")) : null;
            }
        } catch (SQLException e) { throw erro(e); }
    }

    public List<Filme> getFilmes() {
        List<Filme> filmes = new ArrayList<>();
        try (Connection c = conectar(); Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT f.*, g.nome AS genero FROM filmes f JOIN generos g ON g.id=f.genero_id ORDER BY f.id")) {
            while (r.next()) filmes.add(new Filme(r.getInt("id"), r.getString("titulo"), r.getInt("ano"),
                    new Genero(r.getInt("genero_id"), r.getString("genero"))));
            return filmes;
        } catch (SQLException e) { throw erro(e); }
    }

    public Map<Integer, Reacao> getReacoes(Usuario usuario) {
        Map<Integer, Reacao> resultado = new HashMap<>();
        try (Connection c = conectar(); PreparedStatement p = c.prepareStatement(
                "SELECT filme_id,reacao FROM avaliacoes WHERE usuario_id=?")) {
            p.setInt(1, usuario.getId());
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) resultado.put(r.getInt(1), Reacao.valueOf(r.getString(2)));
            }
            return resultado;
        } catch (SQLException e) { throw erro(e); }
    }

    // Uma avaliação por usuário/filme. Trocar o voto atualiza a mesma linha.
    public void salvarReacao(Usuario usuario, Filme filme, Reacao reacao) {
        String sql = reacao == null
                ? "DELETE FROM avaliacoes WHERE usuario_id=? AND filme_id=?"
                : "INSERT INTO avaliacoes(usuario_id,filme_id,reacao) VALUES (?,?,?) ON CONFLICT(usuario_id,filme_id) DO UPDATE SET reacao=excluded.reacao";
        try (Connection c = conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, usuario.getId()); p.setInt(2, filme.getId());
            if (reacao != null) p.setString(3, reacao.name());
            p.executeUpdate();
        } catch (SQLException e) { throw erro(e); }
    }

    private IllegalStateException erro(SQLException e) {
        return new IllegalStateException("Não foi possível acessar ou salvar os dados. Verifique o acesso ao arquivo do banco.", e);
    }
}
