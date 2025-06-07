package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.ArrayList;

import dto.SearchedBook;
import util.DBUtil;
import util.InsertResult;
import util.RemoveResult;

public class BookDAO {
	public InsertResult insertBook(String title, String author, String publisher, String isbn) {
		String sql = "INSERT INTO BOOK (TITLE, AUTHOR, PUBLISHER, ISBN) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);
			pstmt.setString(2, author);
			pstmt.setString(3, publisher);
			pstmt.setString(4, isbn);

			int result = pstmt.executeUpdate();
			System.out.println("result : " + result);

			return result == 1 ? InsertResult.SUCCESS : InsertResult.FAIL;
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return InsertResult.DUPLICATE_KEY;

		} catch (SQLException e) {
			e.printStackTrace();
			return InsertResult.FAIL;
		}
	}

	public List<SearchedBook> selectByBookTitle(String keyword) throws SQLException {
		List<SearchedBook> books = new ArrayList<>();
		String sql = "SELECT b.*, \n" + "       LOCATE(?, REPLACE(b.title, ' ', '')) AS pos, \n" + "       v.status \n"
				+ "FROM book b \n" + "JOIN book_status_view v ON b.book_id = v.book_id \n"
				+ "WHERE REPLACE(b.title, ' ', '') LIKE CONCAT('%', ?, '%') \n" + "ORDER BY \n" + "  CASE \n"
				+ "    WHEN REPLACE(b.title, ' ', '') LIKE CONCAT(?, '%') THEN 1 \n"
				+ "    WHEN REPLACE(b.title, ' ', '') LIKE CONCAT('%', ?, '%') THEN 2 \n" + "    ELSE 3 \n"
				+ "  END, \n" + "  pos, \n" + "  LENGTH(REPLACE(b.title, ' ', '')) ASC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 1; i <= 4; i++) {
				pstmt.setString(i, keyword);
			}

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SearchedBook book = new SearchedBook();
				book.setBookId(rs.getInt("BOOK_ID"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setIsbn(rs.getString("ISBN"));
				book.setStatus(rs.getString("STATUS"));
				book.setImageUrl(rs.getString("IMAGE_URL"));
				books.add(book);
			}
		}
		return books;

	}

	public List<SearchedBook> selectByBookPublisher(String keyword) throws SQLException {
		List<SearchedBook> books = new ArrayList<>();
		String sql = "SELECT b.*, \n" + "       LOCATE(?, REPLACE(b.publisher, ' ', '')) AS pos, \n"
				+ "       COALESCE(bsv.status, '대여가능') AS status \n" + "FROM book b \n"
				+ "LEFT JOIN book_status_view bsv ON b.book_id = bsv.book_id \n"
				+ "WHERE REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', ?, '%') \n" + "ORDER BY \n" + "  CASE \n"
				+ "    WHEN REPLACE(b.publisher, ' ', '') LIKE CONCAT(?, '%') THEN 1 \n"
				+ "    WHEN REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', ?, '%') THEN 2 \n" + "    ELSE 3 \n"
				+ "  END, \n" + "  pos, \n" + "  LENGTH(REPLACE(b.publisher, ' ', '')) ASC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 1; i <= 4; i++) {
				pstmt.setString(i, keyword);
			}

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SearchedBook book = new SearchedBook();
				book.setBookId(rs.getInt("BOOK_ID"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setIsbn(rs.getString("ISBN"));
				book.setStatus(rs.getString("STATUS"));
				book.setImageUrl(rs.getString("IMAGE_URL"));
				books.add(book);
			}
		}
		return books;
	}

	public List<SearchedBook> selectByBookTitleAndPublisher(String title, String publisher) throws SQLException {
		List<SearchedBook> books = new ArrayList<>();
		String sql = "SELECT b.*, \n" + "       LOCATE(?, REPLACE(b.title, ' ', '')) AS title_pos, \n"
				+ "       LOCATE(?, REPLACE(b.publisher, ' ', '')) AS publisher_pos, \n"
				+ "       COALESCE(bsv.status, '대여가능') AS status \n" + "FROM book b \n"
				+ "LEFT JOIN book_status_view bsv ON b.book_id = bsv.book_id \n" + "WHERE \n"
				+ "  REPLACE(b.title, ' ', '') LIKE CONCAT('%', ?, '%') AND \n"
				+ "  REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', ?, '%') \n" + "ORDER BY \n" + "  CASE \n"
				+ "    WHEN REPLACE(b.title, ' ', '') LIKE CONCAT(?, '%') AND REPLACE(b.publisher, ' ', '') LIKE CONCAT(?, '%') THEN 1 \n"
				+ "    WHEN REPLACE(b.title, ' ', '') LIKE CONCAT('%', ?, '%') AND REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', ?, '%') THEN 2 \n"
				+ "    ELSE 3 \n" + "  END, \n" + "  LEAST( \n" + "    NULLIF(title_pos, 0), \n"
				+ "    NULLIF(publisher_pos, 0) \n" + "  ), \n"
				+ "  LENGTH(REPLACE(b.title, ' ', '')) + LENGTH(REPLACE(b.publisher, ' ', '')) ASC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < 8; i++) {
				if ((i + 1) % 2 == 1) { // 1, 3, 5, 7 → 홀수 인덱스는 titleKeyword
					pstmt.setString(i + 1, title);
				} else { // 2, 4, 6, 8 → 짝수 인덱스는 publisherKeyword
					pstmt.setString(i + 1, publisher);
				}
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SearchedBook book = new SearchedBook();
				book.setBookId(rs.getInt("BOOK_ID"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setIsbn(rs.getString("ISBN"));
				book.setStatus(rs.getString("STATUS"));
				book.setImageUrl(rs.getString("IMAGE_URL"));
				books.add(book);
			}
		}
		return books;
	}

	public RemoveResult deleteBook(int bookId) {
		String sql = "DELETE FROM BOOK WHERE book_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, bookId);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0)
				return RemoveResult.SUCCESS;
			else
				return RemoveResult.NOT_FOUND;

		} catch (SQLException e) {
			if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
				return RemoveResult.FOREIGN_KEY_CONSTRAINT;
			}
			e.printStackTrace();
			return RemoveResult.DB_ERROR;
		}
	}

}
