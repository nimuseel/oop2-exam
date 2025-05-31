package gui;
import java.awt.EventQueue;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchBook extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField titleField;
    private JTextField publisherField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    // 예시 데이터: 실제로는 파일/DB에서 불러온 리스트를 사용
    private List<Book> bookList = Arrays.asList(
        new Book("해리포터", "J.K.롤링", "문학동네", "1234567890", "가능"),
        new Book("데미안", "헤르만 헤세", "민음사", "9876543210", "대출중"),
        new Book("자바의 정석", "남궁성", "도우출판", "1111222233334", "가능"),
        new Book("해리포터", "J.K.롤링", "문학동네", "5555666677778", "대출중")
    );

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SearchBook dialog = new SearchBook(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SearchBook(JFrame parent) {
        super(parent, "도서 조회", true);
        setSize(600, 400);
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

        JButton searchBtn = new JButton("조회");
        searchBtn.setBounds(180, 75, 100, 35);
        JButton cancelBtn = new JButton("닫기");
        cancelBtn.setBounds(300, 75, 100, 35);
        getContentPane().add(searchBtn);
        getContentPane().add(cancelBtn);

        // 테이블
        String[] columns = {"도서명", "저자", "출판사", "ISBN", "대출 여부"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(26, 130, 540, 200);
        getContentPane().add(scrollPane);

        // 조회 버튼: 검색 및 결과 표시
        searchBtn.addActionListener(e -> searchBooks());
        cancelBtn.addActionListener(e -> setVisible(false));
    }

    // 검색 및 결과 테이블 표시
    private void searchBooks() {
        String title = titleField.getText().trim();
        String publisher = publisherField.getText().trim();

        tableModel.setRowCount(0); // 기존 결과 삭제

        for (Book b : bookList) {
            boolean match = true;
            if (!title.isEmpty() && !b.title.contains(title)) match = false;
            if (!publisher.isEmpty() && !b.publisher.contains(publisher)) match = false;
            if (match) {
                tableModel.addRow(new Object[]{b.title, b.author, b.publisher, b.isbn, b.status});
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "검색 결과가 없습니다.");
        }
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
