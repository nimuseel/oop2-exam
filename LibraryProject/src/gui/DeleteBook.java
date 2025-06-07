package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dto.SearchedBook;

public class DeleteBook extends JDialog {

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
		EventQueue.invokeLater(() -> {
			try {
				DeleteBook dialog = new DeleteBook(null);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public DeleteBook(JFrame parent) {
		super(parent, "도서 삭제", true);

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
		String[] columns = { "도서명", "저자", "출판사", "ISBN", "대출 여부", "bookId" };
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

		JButton deleteBtn = new JButton("삭제");
		deleteBtn.setBounds(180, 340, 120, 40);
		getContentPane().add(deleteBtn);

		JButton closeBtn = new JButton("닫기");
		closeBtn.setBounds(350, 340, 120, 40);
		getContentPane().add(closeBtn);

		searchBtn.addActionListener(e -> searchBooks());
		deleteBtn.addActionListener(e -> deleteBook());
		closeBtn.addActionListener(e -> dispose());

		// 시작 시에는 항상 테이블 카드가 보이도록 설정
		cardLayout.show(tablePanel, TABLE_CARD);
	}

	private void searchBooks() {
		String title = titleField.getText().trim();
		String publisher = publisherField.getText().trim();

		tableModel.setRowCount(0);

		// 검색어가 없으면 무조건 테이블 카드 보이도록
		if (title.isEmpty() && publisher.isEmpty()) {
			cardLayout.show(tablePanel, TABLE_CARD);
			return;
		}

		try {
			if (!title.isEmpty() && publisher.isEmpty())
				bookList = bookDAO.selectByBookTitle(title);
			else if (title.isEmpty() && !publisher.isEmpty())
				bookList = bookDAO.selectByBookPublisher(publisher);
			else if (!title.isEmpty() && !publisher.isEmpty())
				bookList = bookDAO.selectByBookTitleAndPublisher(title, publisher);
			else
				bookList = new ArrayList<>();
		} catch (SQLException e) {
			e.printStackTrace();
			bookList = new ArrayList<>();
		}

		for (SearchedBook b : bookList) {
			tableModel.addRow(new Object[] { b.getTitle(), b.getAuthor(), b.getPublisher(), b.getIsbn(), b.getStatus(),
					b.getBookId() });
		}

		// 검색 결과가 없으면 empty 카드, 있으면 테이블 카드
		if (tableModel.getRowCount() == 0) {
			cardLayout.show(tablePanel, EMPTY_CARD);
		} else {
			cardLayout.show(tablePanel, TABLE_CARD);
		}
	}

	private void deleteBook() {
		int row = resultTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "삭제할 도서를 선택하세요.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION)
			return;

		int bookId = (int) tableModel.getValueAt(row, 5);

		Iterator<SearchedBook> it = bookList.iterator();
		while (it.hasNext()) {
			SearchedBook book = it.next();
			if (book.getBookId() == bookId) {
				bookDAO.deleteBook(bookId); // 실제 DB 삭제
				it.remove();
				break;
			}
		}

		tableModel.removeRow(row);

		// 삭제 후 테이블이 비었고, 검색어가 있을 때만 empty 카드로 전환
		String title = titleField.getText().trim();
		String publisher = publisherField.getText().trim();
		if (tableModel.getRowCount() == 0 && (!title.isEmpty() || !publisher.isEmpty())) {
			cardLayout.show(tablePanel, EMPTY_CARD);
		}

		JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
	}
}
