package gui;
import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dao.LoanDAO;
import dto.SearchedBook;
import util.SessionContext;

public class BorrowBook extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField titleField;
    private JTextField publisherField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    private List<SearchedBook> bookList = new ArrayList<>();
    private final BookDAO bookDAO = new BookDAO();

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

        // 테이블 구성
        String[] columns = {"도서명", "저자", "출판사", "ISBN", "대출 여부", "bookId"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        resultTable.getColumnModel().getColumn(5).setMinWidth(0);
        resultTable.getColumnModel().getColumn(5).setMaxWidth(0);
        resultTable.getColumnModel().getColumn(5).setWidth(0);
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(26, 80, 584, 240);
        getContentPane().add(scrollPane);

        JButton borrowBtn = new JButton("대출하기");
        borrowBtn.setBounds(180, 340, 120, 40);
        getContentPane().add(borrowBtn);

        JButton closeBtn = new JButton("닫기");
        closeBtn.setBounds(350, 340, 120, 40);
        getContentPane().add(closeBtn);

        // 검색 버튼
        searchBtn.addActionListener(e -> searchBooks());

        // 대출 버튼
        borrowBtn.addActionListener(e -> borrowBook());

        closeBtn.addActionListener(e -> dispose());
    }

    // 검색: 대출 가능 도서만 표시
    private void searchBooks() {
    	String title = titleField.getText().trim();
        String publisher = publisherField.getText().trim();
        
        tableModel.setRowCount(0);

        try {
        	if(!title.isEmpty() && publisher.isEmpty())
        		bookList = bookDAO.selectByBookTitle(title);
        	else if(title.isEmpty() && !publisher.isEmpty())
        		bookList = bookDAO.selectByBookPublisher(publisher);
        	else if(!title.isEmpty() && !publisher.isEmpty())
        		bookList = bookDAO.selectByBookTitleAndPublisher(title,publisher);
     
        } catch (SQLException e) {
            e.printStackTrace();
            bookList = new ArrayList<>();
        }

        for (SearchedBook b : bookList) {
            tableModel.addRow(new Object[]{
                b.getTitle(), b.getAuthor(), b.getPublisher(),
                b.getIsbn(), b.getStatus(), b.getBookId()
            });
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
        int bookId = (int) tableModel.getValueAt(row, 5);
        LoanDAO loanDAO = new LoanDAO();
        loanDAO.borrowBook(bookId, SessionContext.getInstance().getCurrentUserId());
       
        
        JOptionPane.showMessageDialog(this,
            "도서 [" + bookTitle + "] (ISBN: " + bookIsbn + ")\n대출 완료!\n반납일은 대출일로부터 2주입니다.");
        // 대출 후 테이블에서 제거(화면 갱신)
        tableModel.removeRow(row);
    }

}
