package gui;
import javax.swing.*;
import java.awt.event.*;

public class Main extends JDialog {
    private static final long serialVersionUID = 1L;

    public Main() {
        setTitle("도서관 시스템 메인");
        setSize(350, 180);
        setLocationRelativeTo(null);
        setLayout(null);

        JButton loginBtn = new JButton("로그인");
        loginBtn.setBounds(100, 50, 120, 50);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            Signin loginDialog = new Signin(null);
            setVisible(false);
            loginDialog.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	Main main = new Main();
            main.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            main.setVisible(true);
        });
    }
}
