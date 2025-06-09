package gui;
import javax.swing.*;
import java.awt.*;

public class Main extends JDialog {
    private static final long serialVersionUID = 1L;

    public Main() {
        setTitle("도서관 시스템 메인");
        setSize(509, 325);
        setLocationRelativeTo(null);

        // 배경 이미지를 JLabel로 생성
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("background.jpeg")));
        background.setLayout(null); // 기존과 동일하게 null 레이아웃 사용
        setContentPane(background);

        JButton loginBtn = new JButton("로그인");
        loginBtn.setBounds(200, 170, 107, 41);
        background.add(loginBtn);

        JLabel lblWelcome = new JLabel("도서관 시스템에 오신 것을 환영합니다!");
        lblWelcome.setForeground(new Color(233, 233, 233));
        lblWelcome.setBounds(160, 100, 204, 16);
        background.add(lblWelcome);

        JLabel lblInfo = new JLabel("시스템을 이용하시려면 로그인 후 이용해 주세요.");
        lblInfo.setForeground(new Color(0, 0, 0));
        lblInfo.setForeground(new Color(233, 233, 233));
        lblInfo.setBounds(135, 120, 244, 16);
        background.add(lblInfo);

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
