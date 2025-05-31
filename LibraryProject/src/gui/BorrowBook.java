package gui;
import java.awt.EventQueue;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BorrowBook extends JDialog {

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
        new Book("해리포터", "J.K.롤링", "문학동네", "5555666677778", "가능")
    );

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	BorrowBook dialog = new BorrowBook(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BorrowBook(JFrame parent) {
        super(parent, "도서 대출", true);
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
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(26, 80, 584, 200);
        getContentPane().add(scrollPane);

        JButton borrowBtn = new JButton("대출하기");
        borrowBtn.setBounds(193, 300, 120, 40);
        getContentPane().add(borrowBtn);

        JButton closeBtn = new JButton("닫기");
        closeBtn.setBounds(325, 300, 120, 40);
        getContentPane().add(closeBtn);

        // 검색 버튼
        searchBtn.addActionListener(e -> searchBooks());

        // 대출 버튼
        borrowBtn.addActionListener(e -> borrowBook());

        closeBtn.addActionListener(e -> setVisible(false));
    }

    // 검색: 대출 가능 도서만 표시
    private void searchBooks() {
        String title = titleField.getText().trim();
        String publisher = publisherField.getText().trim();

        tableModel.setRowCount(0);

        for (Book b : bookList) {
            boolean match = true;
            if (!title.isEmpty() && !b.title.contains(title)) match = false;
            if (!publisher.isEmpty() && !b.publisher.contains(publisher)) match = false;
            if (!"가능".equals(b.status)) match = false; // 대출 가능만
            if (match) {
                tableModel.addRow(new Object[]{b.title, b.author, b.publisher, b.isbn, b.status});
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "검색 결과가 없습니다.");
        }
    }

    // 대출 처리
    private void borrowBook() {
        int row = resultTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "대출할 도서를 선택하세요.");
            return;
        }
        // 실제로는 여기서 DB/파일에 대출 처리, 상태 변경 등
        String bookTitle = (String) tableModel.getValueAt(row, 0);
        String bookIsbn = (String) tableModel.getValueAt(row, 3);
        JOptionPane.showMessageDialog(this,
            "도서 [" + bookTitle + "] (ISBN: " + bookIsbn + ")\n대출 완료!\n반납일은 대출일로부터 2주입니다.");
        // 대출 후 테이블에서 제거(화면 갱신)
        tableModel.removeRow(row);
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
