package com.letterboxd;
import com.letterboxd.model.*;
import com.letterboxd.controller.PerfilController;
import com.letterboxd.repository.BancoDeDados;
import javax.imageio.ImageIO;

public class ReacoesTest {
    static void check(boolean b) { if (!b) throw new AssertionError(); }
    public static void main(String[] args) throws Exception {
        BancoDeDados db = BancoDeDados.getInstance();
        Usuario a = db.autenticar("user","1234"), b = db.autenticar("user2","1234");
        check(a != null && b != null && db.autenticar("user","errada") == null);
        PerfilController c = new PerfilController(a);
        Filme f = db.getFilmes().get(0);
        if (args.length > 0 && args[0].equals("reabrir")) {
            check(c.getReacoes().get(f.getId()) == Reacao.LIKE);
            check(db.getReacoes(b).get(f.getId()) == Reacao.DISLIKE);
            System.out.println("OK: persistência entre processos e isolamento de usuários."); return;
        }
        check(c.getReacoes().isEmpty());
        c.avaliar(f,Reacao.LIKE); check(c.getReacoes().get(f.getId()) == Reacao.LIKE);
        check(db.getReacoes(b).isEmpty());
        c.avaliar(f,Reacao.DISLIKE);
        check(c.getReacoes().size() == 1 && c.getReacoes().get(f.getId()) == Reacao.DISLIKE);
        c.avaliar(f,Reacao.DISLIKE); check(c.getReacoes().isEmpty());
        c.avaliar(f,Reacao.LIKE); c.avaliar(f,Reacao.LIKE); check(c.getReacoes().isEmpty());
        c.avaliar(f,Reacao.LIKE);
        db.salvarReacao(b,f,Reacao.DISLIKE);
        for (Filme filme : db.getFilmes())
            check(ImageIO.read(ReacoesTest.class.getResource(filme.getPoster())) != null);
        System.out.println("OK: login, like, dislike, troca, remoção, isolamento e três pôsteres.");
    }
}
