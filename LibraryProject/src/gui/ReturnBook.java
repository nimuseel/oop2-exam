package gui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReturnBook extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField titleField;
    private JTextField publisherField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    private List<Book> bookList = Arrays.asList();

    // CardLayout 관련 필드
    private JPanel tablePanel;
    private CardLayout cardLayout;
    private static final String TABLE_CARD = "table";
    private static final String EMPTY_CARD = "empty";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ReturnBook dialog = new ReturnBook(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ReturnBook(JFrame parent) {
        super(parent, "도서 반납", true);
        setSize(650, 480);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);

        JLabel titleLabel = new JLabel("도서명:");
        titleLabel.setBounds(26, 20, 59, 40);
        getContentPane().add(titleLabel);
        titleField = new JTextField();
        titleField.setBounds(120, 20, 150, 40);
        getContentPane().add(titleField);

        JLabel publisherLabel = new JLabel("출판사:");
        publisherLabel.setBounds(290, 20, 59, 40);
        getContentPane().add(publisherLabel);
        publisherField = new JTextField();
        publisherField.setBounds(360, 20, 150, 40);
        getContentPane().add(publisherField);

        JButton searchBtn = new JButton("검색");
        searchBtn.setBounds(530, 20, 80, 40);
        getContentPane().add(searchBtn);

        // 테이블
        String[] columns = {"도서명", "저자", "출판사", "ISBN", "대출 여부"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        resultTable = new JTable(tableModel);

        // CardLayout으로 테이블 영역 구성
        cardLayout = new CardLayout();
        tablePanel = new JPanel(cardLayout);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        tablePanel.add(scrollPane, TABLE_CARD);

        JLabel emptyLabel = new JLabel("검색 결과가 없습니다.", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        tablePanel.add(emptyLabel, EMPTY_CARD);

        tablePanel.setBounds(26, 80, 584, 240);
        getContentPane().add(tablePanel);

        JButton returnBtn = new JButton("반납");
        returnBtn.setBounds(180, 340, 120, 40);
        getContentPane().add(returnBtn);

        JButton closeBtn = new JButton("닫기");
        closeBtn.setBounds(350, 340, 120, 40);
        getContentPane().add(closeBtn);

        // 검색 버튼
        searchBtn.addActionListener(e -> searchBooks());

        // 반납 버튼
        returnBtn.addActionListener(e -> returnBook());

        closeBtn.addActionListener(e -> setVisible(false));

        // 시작 시에는 항상 테이블 카드가 보이도록 설정
        cardLayout.show(tablePanel, TABLE_CARD);
    }

    // 검색: 대출중인 도서만 표시
    private void searchBooks() {
        String title = titleField.getText().trim();
        String publisher = publisherField.getText().trim();

        tableModel.setRowCount(0);

        // 검색어가 없으면 무조건 테이블 카드 보이도록
        if (title.isEmpty() && publisher.isEmpty()) {
            cardLayout.show(tablePanel, TABLE_CARD);
            return;
        }

        for (Book b : bookList) {
            boolean match = true;
            if (!title.isEmpty() && !b.title.contains(title)) match = false;
            if (!publisher.isEmpty() && !b.publisher.contains(publisher)) match = false;
            if (!"대출중".equals(b.status)) match = false; // 대출중만
            if (match) {
                tableModel.addRow(new Object[]{b.title, b.author, b.publisher, b.isbn, b.status});
            }
        }

        // 검색 결과가 없으면 empty 카드, 있으면 테이블 카드
        if (tableModel.getRowCount() == 0) {
            cardLayout.show(tablePanel, EMPTY_CARD);
        } else {
            cardLayout.show(tablePanel, TABLE_CARD);
        }
    }

    // 반납 처리
    private void returnBook() {
        int row = resultTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "반납할 도서를 선택하세요.");
            return;
        }
        String isbn = (String) tableModel.getValueAt(row, 3);

        // 실제로는 파일/DB에서 상태 변경 필요
        for (Book b : bookList) {
            if (b.isbn.equals(isbn)) {
                b.status = "가능";
                break;
            }
        }
        tableModel.removeRow(row);

        // 반납 후 테이블이 비었고, 검색어가 있을 때만 empty 카드로 전환
        String title = titleField.getText().trim();
        String publisher = publisherField.getText().trim();
        if (tableModel.getRowCount() == 0 && (!title.isEmpty() || !publisher.isEmpty())) {
            cardLayout.show(tablePanel, EMPTY_CARD);
        }

        JOptionPane.showMessageDialog(this, "도서가 반납되었습니다.");
    }

    // Book 클래스 (내부 static 클래스로 예시)
    static class Book {
        String title, author, publisher, isbn, status;
        public Book(String title, String author, String publisher, String isbn, String status) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.isbn = isbn;
            this.status = status;
        }
    }
}
