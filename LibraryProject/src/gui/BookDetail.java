package gui;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class BookDetail extends JDialog {

    public BookDetail(Window parent, String title, String author, String publisher, String isbn, String status, String imageUrl) {
        super(parent, "도서 상세 정보", ModalityType.APPLICATION_MODAL);

        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 이미지 패널을 항상 고정 크기로 생성
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(400, 220));
        imagePanel.setBackground(Color.WHITE);

        JLabel imageLabel;
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                URL url = new URI(imageUrl).toURL();
                ImageIcon icon = new ImageIcon(url);
                // 이미지 크기 조정
                Image img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(img));
            } else {
                imageLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
                imageLabel.setPreferredSize(new Dimension(150, 200));
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel = new JLabel("이미지 로드 실패", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(150, 200));
        }
        // 항상 중앙에 위치
        imagePanel.add(imageLabel, new GridBagConstraints());

        add(imagePanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1, 0, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        infoPanel.add(new JLabel("제목: " + title));
        infoPanel.add(new JLabel("저자: " + author));
        infoPanel.add(new JLabel("출판사: " + publisher));
        infoPanel.add(new JLabel("ISBN: " + isbn));
        infoPanel.add(new JLabel("대출 여부: " + status));
        add(infoPanel, BorderLayout.CENTER);

        JButton closeBtn = new JButton("닫기");
        closeBtn.addActionListener(e -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}
