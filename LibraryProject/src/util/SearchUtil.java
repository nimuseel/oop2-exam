package util;

import java.awt.CardLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dto.SearchedBook;

public class SearchUtil {
	private final static BookDAO bookDAO = new BookDAO();
	private static final String TABLE_CARD = "table";
    private static final String EMPTY_CARD = "empty";
	
	public static  List<SearchedBook> searchBooks(DefaultTableModel tableModel, CardLayout cardLayout, JPanel tablePanel) {
		 List<SearchedBook> bookList = new ArrayList<>();
		tableModel.setRowCount(0);
		
		try {
			bookList = bookDAO.selectAllBooks();
		} catch (SQLException e) {
			e.printStackTrace();
			bookList = new ArrayList<>();
		}

		for (SearchedBook b : bookList) {
			tableModel.addRow(new Object[] { b.getTitle(), b.getAuthor(), b.getPublisher(), b.getIsbn(), b.getStatus(),
					b.getBookId(), b.getImageUrl()});
		}

		// 검색 결과가 없으면 empty 카드, 있으면 테이블 카드
		if (tableModel.getRowCount() == 0) {
			cardLayout.show(tablePanel, EMPTY_CARD);
		} else {
			cardLayout.show(tablePanel, TABLE_CARD);
		}
		
		return bookList;
	}

	public static List<SearchedBook> searchBooks(DefaultTableModel tableModel, CardLayout cardLayout, JPanel tablePanel, String title, String publisher) {
		List<SearchedBook> bookList = new ArrayList<>();
		tableModel.setRowCount(0);

		try {
			if (!title.isEmpty() && publisher.isEmpty())
				bookList = bookDAO.selectByBookTitle(title, false);
			else if (title.isEmpty() && !publisher.isEmpty())
				bookList = bookDAO.selectByBookPublisher(publisher, false);
			else if (!title.isEmpty() && !publisher.isEmpty())
				bookList = bookDAO.selectByBookTitleAndPublisher(title, publisher, false);
			else
				bookList = new ArrayList<>();
		} catch (SQLException e) {
			e.printStackTrace();
			bookList = new ArrayList<>();
		}

		for (SearchedBook b : bookList) {
			tableModel.addRow(new Object[] { b.getTitle(), b.getAuthor(), b.getPublisher(), b.getIsbn(), b.getStatus(),
					b.getBookId(), b.getImageUrl() });
		}

		// 검색 결과가 없으면 empty 카드, 있으면 테이블 카드
		if (tableModel.getRowCount() == 0) {
			cardLayout.show(tablePanel, EMPTY_CARD);
		} else {
			cardLayout.show(tablePanel, TABLE_CARD);
		}
		
		return bookList;
	}
}
