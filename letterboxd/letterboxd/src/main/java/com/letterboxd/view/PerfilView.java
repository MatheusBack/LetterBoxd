package com.letterboxd.view;

import com.letterboxd.controller.PerfilController;
import com.letterboxd.model.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;
import java.util.Map;

public class PerfilView extends JFrame {
    private final PerfilController controller;
    private final JPanel conteudo = new JPanel(new BorderLayout(12,12));
    private final JLabel status = new JLabel("Escolha um filme para avaliar.");
    private boolean perfil;
    private final Color fundo = new Color(225,225,225);

    public PerfilView(Usuario usuario) {
        controller = new PerfilController(usuario);
        setTitle("letterboxd");
        setSize(940,740);
        setMinimumSize(new Dimension(880,650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel main = new JPanel(new BorderLayout(15,15));
        main.setBackground(fundo);
        main.setBorder(BorderFactory.createEmptyBorder(15,25,15,25));
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        JLabel logo = new JLabel("letterboxd");
        logo.setFont(new Font("SansSerif",Font.BOLD,24));
        topo.add(logo,BorderLayout.WEST);
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav.setOpaque(false);
        JButton inicio = botao("Início");
        JButton btnPerfil = botao("Perfil");
        JButton sair = botao("Sair");
        inicio.addActionListener(e -> navegar(false));
        btnPerfil.addActionListener(e -> navegar(true));
        sair.addActionListener(e -> { new LoginView().setVisible(true); dispose(); });
        nav.add(inicio); nav.add(btnPerfil); nav.add(sair);
        topo.add(nav,BorderLayout.EAST);
        main.add(topo,BorderLayout.NORTH);
        conteudo.setOpaque(false);
        main.add(conteudo,BorderLayout.CENTER);
        status.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        status.setOpaque(true); status.setBackground(Color.WHITE);
        main.add(status,BorderLayout.SOUTH);
        setContentPane(main);
        atualizar();
    }

    private void navegar(boolean destino) {
        perfil = destino;
        try { atualizar(); } catch (RuntimeException ex) { erro(ex); }
    }

    private void atualizar() {
        Map<Integer, Reacao> reacoes = controller.getReacoes();
        JPanel novo = new JPanel(new BorderLayout(12,12));
        novo.setOpaque(false);
        JPanel titulo = new JPanel(new GridLayout(0,1,0,6));
        titulo.setOpaque(false);
        JLabel nome = new JLabel(perfil ? "Perfil de " + controller.getUsuario().getNomeExibicao() : "Avaliação de filmes");
        nome.setFont(new Font("SansSerif",Font.PLAIN,27)); titulo.add(nome);
        long likes = reacoes.values().stream().filter(r -> r == Reacao.LIKE).count();
        titulo.add(new JLabel(perfil
                ? "@" + controller.getUsuario().getUsername() + "  •  " + likes + " likes  •  " + (reacoes.size()-likes) + " dislikes"
                : "Veja alguns filmes e diga se você gostou."));
        titulo.add(new JLabel("Clique novamente na opção marcada para remover sua avaliação."));
        novo.add(titulo,BorderLayout.NORTH);
        if (perfil) {
            JTabbedPane abas = new JTabbedPane();
            abas.addTab("Gostei (" + likes + ")", lista(reacoes, Reacao.LIKE));
            abas.addTab("Não gostei (" + (reacoes.size()-likes) + ")", lista(reacoes, Reacao.DISLIKE));
            // Preserve the selected tab after changing a reaction.
            if (conteudo.getComponentCount() > 0) {
                Component anterior = ((JPanel)conteudo.getComponent(0)).getComponent(1);
                if (anterior instanceof JTabbedPane)
                    abas.setSelectedIndex(((JTabbedPane)anterior).getSelectedIndex());
            }
            novo.add(abas,BorderLayout.CENTER);
        } else novo.add(lista(reacoes,null),BorderLayout.CENTER);
        conteudo.removeAll(); conteudo.add(novo);
        conteudo.revalidate(); conteudo.repaint();
    }

    private JComponent lista(Map<Integer, Reacao> reacoes, Reacao filtro) {
        JPanel cards = new JPanel(new GridLayout(1,3,15,0));
        cards.setBackground(fundo);
        int quantidade = 0;
        for (Filme filme : controller.getFilmes()) {
            if (filtro == null || reacoes.get(filme.getId()) == filtro) {
                cards.add(card(filme,reacoes.get(filme.getId()))); quantidade++;
            }
        }
        if (quantidade == 0) return new JLabel("Nenhum filme nesta lista ainda. Avalie os filmes em Início.",SwingConstants.CENTER);
        while (quantidade++ < 3) { JPanel espaco = new JPanel(); espaco.setOpaque(false); cards.add(espaco); }
        return cards;
    }

    private JPanel card(Filme filme, Reacao atual) {
        JPanel card = new JPanel(new BorderLayout(8,8));
        card.setBackground(new Color(210,210,210));
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(160,160,160)),
                BorderFactory.createEmptyBorder(10,10,10,10)));
        JLabel imagem = new JLabel("",SwingConstants.CENTER);
        URL recurso = getClass().getResource(filme.getPoster());
        if (recurso != null) {
            ImageIcon original = new ImageIcon(recurso);
            double escala = Math.min(230.0/original.getIconWidth(),330.0/original.getIconHeight());
            imagem.setIcon(new ImageIcon(original.getImage().getScaledInstance(
                    (int)(original.getIconWidth()*escala),(int)(original.getIconHeight()*escala),Image.SCALE_SMOOTH)));
            imagem.getAccessibleContext().setAccessibleName("Pôster de " + filme.getTitulo());
        } else imagem.setText("Imagem indisponível");
        card.add(imagem,BorderLayout.CENTER);
        JPanel info = new JPanel(new GridLayout(0,1,0,7)); info.setOpaque(false);
        JLabel titulo = new JLabel(filme.getTitulo()); titulo.setFont(new Font("SansSerif",Font.BOLD,16));
        info.add(titulo); info.add(new JLabel("Ano: " + filme.getAnoLancamento()));
        info.add(new JLabel(filme.getGenero().getNome()));
        JPanel botoes = new JPanel(new GridLayout(1,2,5,0)); botoes.setOpaque(false);
        JButton like = botao(atual == Reacao.LIKE ? "✓ Gostei" : "Gostei");
        JButton dislike = botao(atual == Reacao.DISLIKE ? "✓ Não gostei" : "Não gostei");
        if (atual == Reacao.LIKE) like.setBackground(new Color(155,220,170));
        if (atual == Reacao.DISLIKE) dislike.setBackground(new Color(245,175,175));
        like.addActionListener(e -> avaliar(filme,Reacao.LIKE));
        dislike.addActionListener(e -> avaliar(filme,Reacao.DISLIKE));
        botoes.add(like); botoes.add(dislike); info.add(botoes);
        card.add(info,BorderLayout.SOUTH);
        return card;
    }

    private void avaliar(Filme filme, Reacao reacao) {
        try {
            controller.avaliar(filme,reacao);
            atualizar();
            status.setText("Avaliação de “" + filme.getTitulo() + "” atualizada no seu perfil.");
        } catch (RuntimeException ex) { erro(ex); }
    }

    private void erro(RuntimeException ex) {
        JOptionPane.showMessageDialog(this,ex.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
    }

    private JButton botao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.WHITE); btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif",Font.PLAIN,12)); btn.setFocusPainted(false);
        return btn;
    }
}
