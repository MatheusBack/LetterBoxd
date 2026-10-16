package com.letterboxd.view;

import com.letterboxd.model.Usuario;
import com.letterboxd.repository.BancoDeDados;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public LoginView() {
        setTitle("Letterboxd - Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField("user");
        panel.add(txtUsuario);

        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField("1234");
        panel.add(txtSenha);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> efetuarLogin());

        panel.add(new JLabel()); // Espaçador
        panel.add(btnLogin);

        add(panel);
    }

    private void efetuarLogin() {
        String user = txtUsuario.getText();
        String pass = new String(txtSenha.getPassword());

        Usuario usuarioLogado = BancoDeDados.getInstance().autenticar(user, pass);

        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuarioLogado.getNomeExibicao() + "!");
            new PerfilView(usuarioLogado).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}