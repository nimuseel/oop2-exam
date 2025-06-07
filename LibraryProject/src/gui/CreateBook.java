package gui;

import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.BookDAO;
import util.InsertResult;

public class CreateBook extends JDialog {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CreateBook dialog = new CreateBook(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CreateBook(JFrame parent) {
        super(parent, "도서 정보 입력", true);
        setSize(468, 430);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);

        JLabel bookName = new JLabel("제목:");
        bookName.setBounds(26, 1, 59, 61);
        getContentPane().add(bookName);
        JTextField titleField = new JTextField("");
        titleField.setBounds(169, 1, 229, 61);
        getContentPane().add(titleField);

        JLabel authorLabel = new JLabel("저자:");
        authorLabel.setBounds(26, 74, 59, 61);
        getContentPane().add(authorLabel);
        JTextField authorField = new JTextField("");
        authorField.setBounds(169, 74, 229, 61);
        getContentPane().add(authorField);

        JLabel publisherLabel = new JLabel("출판사:");
        publisherLabel.setBounds(26, 145, 59, 61);
        getContentPane().add(publisherLabel);
        JTextField publisherField = new JTextField("");
        publisherField.setBounds(169, 145, 229, 61);
        getContentPane().add(publisherField);

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setBounds(26, 214, 59, 61);
        getContentPane().add(isbnLabel);
        JTextField isbnField = new JTextField("");
        isbnField.setBounds(169, 214, 229, 61);
        getContentPane().add(isbnField);

        // 이미지 경로 필드 추가
        JLabel imageUrlLabel = new JLabel("이미지 경로:");
        imageUrlLabel.setBounds(26, 285, 100, 61);
        getContentPane().add(imageUrlLabel);
        JTextField imageUrlField = new JTextField("");
        imageUrlField.setBounds(169, 285, 229, 61);
        getContentPane().add(imageUrlField);

        // 버튼 위치 조정
        JButton saveBtn = new JButton("저장");
        saveBtn.setBounds(47, 356, 182, 61);
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(239, 356, 182, 61);
        getContentPane().add(saveBtn);
        getContentPane().add(cancelBtn);

        saveBtn.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            String isbn = isbnField.getText();
            String imageUrl = imageUrlField.getText();

            BookDAO bookDAO = new BookDAO();
            // imageUrl 파라미터 추가
            InsertResult ir = bookDAO.insertBook(title, author, publisher, isbn, imageUrl);
            if(ir == InsertResult.FAIL) {
                JOptionPane.showMessageDialog(this, "예기치 못한 오류입니다.", "오류발생", JOptionPane.ERROR_MESSAGE);
                return;
            } else if(ir == InsertResult.SUCCESS) {
                JOptionPane.showMessageDialog(this, "도서가 등록되었습니다.", "등록성공", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }
}
