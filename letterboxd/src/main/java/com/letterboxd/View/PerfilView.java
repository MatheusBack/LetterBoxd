package com.letterboxd.view;

import com.letterboxd.model.Usuario;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PerfilView extends JFrame {

    public PerfilView(Usuario usuario) {
        setTitle("letterboxd");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel Principal com o fundo cinza claro exato do protótipo
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(225, 225, 225));
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // 1. topo (Barra superior com 'letterboxd' e botões 'Início' / 'Filmes')
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel lblLogo = new JLabel("letterboxd");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setForeground(Color.BLACK);
        topBar.add(lblLogo, BorderLayout.WEST);

        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);

        JButton btnInicio = criarBotaoEstilo("Início");
        JButton btnFilmes = criarBotaoEstilo("Filmes");

        navButtons.add(btnInicio);
        navButtons.add(btnFilmes);
        topBar.add(navButtons, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // 2. Conteúdo Central (Título, Subtítulo, Cards e Caixa de Texto)
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Título e Subtítulo
        JLabel lblTitulo = new JLabel("Avaliação de filmes");
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 28));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Veja alguns filmes e diga se você gostou.");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(lblSubtitulo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel de Cards dos 3 Filmes
        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsContainer.setOpaque(false);
        cardsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Filme 1: Ta dando onda
        cardsContainer.add(criarCardFilme("Ta dando onda", "Ano: 2007"));
        // Filme 2: Perfect Blue
        cardsContainer.add(criarCardFilme("Perfect Blue", "Ano: 1997"));
        // Filme 3: O Brutalista
        cardsContainer.add(criarCardFilme("O Brutalista", "Ano: 2024"));

        contentPanel.add(cardsContainer);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Caixa de aviso/comentário inferior
        JPanel textContainer = new JPanel(new BorderLayout());
        textContainer.setBackground(Color.WHITE);
        textContainer.setBorder(new LineBorder(new Color(160, 160, 160), 1));
        textContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblStatus = new JLabel("  Escolha um filme para avaliar.");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblStatus.setPreferredSize(new Dimension(800, 35));
        textContainer.add(lblStatus, BorderLayout.CENTER);

        contentPanel.add(textContainer);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // 3. Rodapé ("Projeto simples de filmes")
        JLabel lblFooter = new JLabel("Projeto simples de filmes", SwingConstants.CENTER);
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFooter.setForeground(Color.BLACK);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        mainPanel.add(lblFooter, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Método para criar cada Card exatamente como na imagem
    private JPanel criarCardFilme(String nomeFilme, String ano) {
        JPanel card = new JPanel();
        card.setBackground(new Color(210, 210, 210));
        card.setBorder(new LineBorder(new Color(160, 160, 160), 1));
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(160, 160, 160), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Área da Foto
        JPanel photoPanel = new JPanel(new GridBagLayout());
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setBorder(new LineBorder(new Color(170, 170, 170), 1));
        photoPanel.setPreferredSize(new Dimension(200, 230));

        JLabel lblFoto = new JLabel("FOTO");
        lblFoto.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblFoto.setForeground(new Color(160, 160, 160));
        photoPanel.add(lblFoto);

        card.add(photoPanel, BorderLayout.CENTER);

        // Informações e Botões abaixo da foto
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblNome = new JLabel(nomeFilme);
        lblNome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAno = new JLabel(ano);
        lblAno.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblAno.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblNome);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblAno);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Botões Gostei / Não gostei
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnGostei = criarBotaoEstilo("Gostei");
        JButton btnNaoGostei = criarBotaoEstilo("Não gostei");

        btnPanel.add(btnGostei);
        btnPanel.add(btnNaoGostei);

        infoPanel.add(btnPanel);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }

    // Botão reto padrão cinza/branco como o do protótipo
    private JButton criarBotaoEstilo(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(160, 160, 160), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return btn;
    }
}