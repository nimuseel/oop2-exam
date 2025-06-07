package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dao.LoanDAO;
import dto.SearchedBook;
import util.SearchUtil;
import util.SessionContext;

public class BorrowBook extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField titleField;
	private JTextField publisherField;
	private JTable resultTable;
	private DefaultTableModel tableModel;

	private List<SearchedBook> bookList = new ArrayList<>();
	private final BookDAO bookDAO = new BookDAO();

	// CardLayout 관련 필드
	private JPanel tablePanel;
	private CardLayout cardLayout;
	private static final String TABLE_CARD = "table";
	private static final String EMPTY_CARD = "empty";

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
		String[] columns = { "도서명", "저자", "출판사", "ISBN", "대출 여부", "bookId", "이미지URL" };
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		resultTable = new JTable(tableModel);
		// bookId, 이미지URL 컬럼 숨기기
		resultTable.getColumnModel().getColumn(5).setMinWidth(0);
		resultTable.getColumnModel().getColumn(5).setMaxWidth(0);
		resultTable.getColumnModel().getColumn(5).setWidth(0);
		resultTable.getColumnModel().getColumn(6).setMinWidth(0);
		resultTable.getColumnModel().getColumn(6).setMaxWidth(0);
		resultTable.getColumnModel().getColumn(6).setWidth(0);

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

		JButton borrowBtn = new JButton("대출하기");
		borrowBtn.setBounds(180, 340, 120, 40);
		getContentPane().add(borrowBtn);

		JButton closeBtn = new JButton("닫기");
		closeBtn.setBounds(350, 340, 120, 40);
		getContentPane().add(closeBtn);

		 searchBtn.addActionListener(e -> 
			bookList = SearchUtil.searchBooks(tableModel, cardLayout, tablePanel, titleField.getText(), publisherField.getText()));
	     // 대출 버튼
	     borrowBtn.addActionListener(e -> borrowBook());
	     closeBtn.addActionListener(e -> dispose());
	
	     // 시작 시에는 항상 테이블 카드가 보이도록 설정
	     cardLayout.show(tablePanel, TABLE_CARD);
	     bookList = SearchUtil.searchBooks(tableModel, cardLayout, tablePanel);

		// 대출 버튼
		borrowBtn.addActionListener(e -> borrowBook());

		closeBtn.addActionListener(e -> dispose());

		// 테이블 row 클릭 시 상세 정보 다이얼로그
		resultTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
					int row = resultTable.getSelectedRow();
					if (row != -1) {
						String title = (String) tableModel.getValueAt(row, 0);
						String author = (String) tableModel.getValueAt(row, 1);
						String publisher = (String) tableModel.getValueAt(row, 2);
						String isbn = (String) tableModel.getValueAt(row, 3);
						String status = (String) tableModel.getValueAt(row, 4);
						String imageUrl = (String) tableModel.getValueAt(row, 6);
						new BookDetail(null, title, author, publisher, isbn, status, imageUrl).setVisible(true);
					}
				}
			}
		});

		// 시작 시에는 항상 테이블 카드가 보이도록 설정
		cardLayout.show(tablePanel, TABLE_CARD);
	}

	// 대출 처리
	private void borrowBook() {
		int row = resultTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "대출할 도서를 선택하세요.");
			return;
		}
		String bookTitle = (String) tableModel.getValueAt(row, 0);
		String bookIsbn = (String) tableModel.getValueAt(row, 3);
		int bookId = (int) tableModel.getValueAt(row, 5);
		LoanDAO loanDAO = new LoanDAO();
		loanDAO.borrowBook(bookId, SessionContext.getInstance().getCurrentUserId());

		JOptionPane.showMessageDialog(this,
				"도서 [" + bookTitle + "] (ISBN: " + bookIsbn + ")\n대출 완료!\n반납일은 대출일로부터 2주입니다.");

		tableModel.removeRow(row);

		// 만약 테이블이 비면, 검색어가 있는 상태에서만 empty 카드로 전환
		String title = titleField.getText().trim();
		String publisher = publisherField.getText().trim();
		if (tableModel.getRowCount() == 0 && (!title.isEmpty() || !publisher.isEmpty())) {
			cardLayout.show(tablePanel, EMPTY_CARD);
		}
	}

}
