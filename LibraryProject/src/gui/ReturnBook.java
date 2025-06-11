package gui;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.LoanDAO;
import dto.SearchedLoan;
import util.SessionContext;

public class ReturnBook extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final LoanDAO loanDAO = new LoanDAO();

	private JTextField titleField;
	private JTextField publisherField;
	private JTable resultTable;
	private DefaultTableModel tableModel;

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
		super(parent, SessionContext.getInstance().getCurrentUserId() + "님" +  " - 도서 반납", true);
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
		String[] columns = { "도서명", "저자", "출판사", "ISBN", "대출일", "반납예정일", "반납상태", "loanId" };
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		resultTable = new JTable(tableModel);

		resultTable.getColumnModel().getColumn(7).setMinWidth(0);
		resultTable.getColumnModel().getColumn(7).setMaxWidth(0);
		resultTable.getColumnModel().getColumn(7).setWidth(0);

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
		searchBtn.addActionListener(e -> searchLoan());

		// 반납 버튼
		returnBtn.addActionListener(e -> returnBook());

		closeBtn.addActionListener(e -> dispose());

		// 시작 시에는 항상 테이블 카드가 보이도록 설정
		cardLayout.show(tablePanel, TABLE_CARD);
		setTable(loanDAO.searchLoan());

	}

	private void searchLoan() {
		String title = titleField.getText().trim();
		String publisher = publisherField.getText().trim();
		
		if (!title.isEmpty() && publisher.isEmpty())
			setTable(loanDAO.searchLoansByTitle(title));
		else if (title.isEmpty() && !publisher.isEmpty())
			setTable(loanDAO.searchLoansByPublisher(publisher));
		else if (!title.isEmpty() && !publisher.isEmpty())
			setTable(loanDAO.searchLoansByTitleAndPublisher(title, publisher));

	}

	private void setTable(List<SearchedLoan> loanList) {
		tableModel.setRowCount(0);
		for (SearchedLoan loan : loanList) {
			tableModel.addRow(new Object[] { loan.getTitle(), loan.getAuthor(), loan.getPublisher(), loan.getIsbn(),
					loan.getLoanDate(), loan.getDueDate(), loan.isReturned() ? "반납됨" : "미반납", loan.getLoanId() });
		}
	}

	// 반납 처리
	private void returnBook() {
		int row = resultTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "반납할 도서를 선택하세요.");
			return;
		}

		int loanId = (int)tableModel.getValueAt(row, 7);
		tableModel.removeRow(row);
		
		boolean isReturned = loanDAO.returnBook(loanId);
		// 반납 후 테이블이 비었고, 검색어가 있을 때만 empty 카드로 전환
		if (isReturned) {
			JOptionPane.showMessageDialog(this, "도서가 반납되었습니다.");
			titleField.setText("");
			publisherField.setText("");
			setTable(loanDAO.searchLoan());
		}
	}
}
