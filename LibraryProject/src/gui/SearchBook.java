package gui;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.SearchedBook;
import util.SearchUtil;

public class SearchBook extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField titleField;
    private JTextField publisherField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private List<SearchedBook> bookList = new ArrayList<>();

    // CardLayout 관련 필드
    private JPanel tablePanel;
    private CardLayout cardLayout;
    private static final String TABLE_CARD = "table";
    private static final String EMPTY_CARD = "empty";

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
        String[] columns = {"도서명", "저자", "출판사", "ISBN", "대출 여부", "bookId", "이미지URL"};
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

        tablePanel.setBounds(26, 130, 540, 200);
        getContentPane().add(tablePanel);

        // 조회 버튼: 검색 및 결과 표시
        searchBtn.addActionListener(e -> 
			bookList = SearchUtil.searchBooks(tableModel, cardLayout, tablePanel, titleField.getText(), publisherField.getText()));
        cancelBtn.addActionListener(e -> dispose());

        // 시작 시에는 항상 테이블 카드가 보이도록 설정
        cardLayout.show(tablePanel, TABLE_CARD);
        SearchUtil.searchBooks(tableModel, cardLayout, tablePanel);

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

    }
}
