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
        try (Connection conn = DBUtil.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setString(4, isbn);
            
            int result = pstmt.executeUpdate();
            System.out.println("result : "+result);
            
            return result == 1 ? InsertResult.SUCCESS : InsertResult.FAIL;
        } catch (SQLIntegrityConstraintViolationException e) {
        	e.printStackTrace();
            return InsertResult.DUPLICATE_KEY;

        } catch (SQLException e) {
            e.printStackTrace();
            return InsertResult.FAIL;
        }		
	}
	
	public List<SearchedBook> selectByBookTitle(String keyword) throws SQLException{
		List<SearchedBook> books = new ArrayList<>();
		String sql = 
			    "SELECT b.*, " +
			    "       LOCATE(?, REPLACE(b.title, ' ', '')) AS pos, " +
			    "       CASE " +
			    "           WHEN EXISTS ( " +
			    "               SELECT 1 " +
			    "               FROM loan l " +
			    "               WHERE l.book_id = b.book_id AND l.is_returned = 0 " +
			    "           ) THEN '대여중' " +
			    "           ELSE '대여가능' " +
			    "       END AS status " +
			    "FROM book b " +
			    "WHERE REPLACE(b.title, ' ', '') LIKE CONCAT('%', ?, '%') " +
			    "ORDER BY " +
			    "  CASE " +
			    "    WHEN REPLACE(b.title, ' ', '') LIKE CONCAT(?, '%') THEN 1 " +
			    "    WHEN REPLACE(b.title, ' ', '') LIKE CONCAT('%', ?, '%') THEN 2 " +
			    "    ELSE 3 " +
			    "  END, " +
			    "  pos, " +
			    "  LENGTH(REPLACE(b.title, ' ', '')) ASC";

		try (Connection conn = DBUtil.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
		            books.add(book);
		        }
		    }
	    return books;

	}
	
	public List<SearchedBook> selectByBookPublisher(String keyword) throws SQLException {
		List<SearchedBook> books = new ArrayList<>();
		String sql = 
			    "SELECT b.*, " +
			    "       LOCATE(?, REPLACE(b.publisher, ' ', '')) AS pos, " +
			    "       CASE " +
			    "           WHEN EXISTS ( " +
			    "               SELECT 1 " +
			    "               FROM loan l " +
			    "               WHERE l.book_id = b.book_id AND l.is_returned = 0 " +
			    "           ) THEN '대여중' " +
			    "           ELSE '대여가능' " +
			    "       END AS status " +
			    "FROM book b " +
			    "WHERE REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', ?, '%') " +
			    "ORDER BY " +
			    "  CASE " +
			    "    WHEN REPLACE(b.publisher, ' ', '') LIKE CONCAT(?, '%') THEN 1 " +
			    "    WHEN REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', ?, '%') THEN 2 " +
			    "    ELSE 3 " +
			    "  END, " +
			    "  pos, " +
			    "  LENGTH(REPLACE(b.publisher, ' ', '')) ASC";

		try (Connection conn = DBUtil.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
		            books.add(book);
		        }
		    }
	    return books;
	}
	
	public List<SearchedBook> selectByBookTitleAndPublisher(String title, String publisher) throws SQLException {
		List<SearchedBook> books = new ArrayList<>();
		String sql = 
			"SELECT " +
		    "    b.book_id, " +
		    "    b.title, " +
		    "    b.author, " +
		    "    b.publisher, " +
		    "    b.isbn, " +
		    "    CASE WHEN l.book_id IS NULL THEN '대여 가능' ELSE '대여 중' END AS status, " +
		    "    LOCATE(REPLACE(?, ' ', ''), REPLACE(b.title, ' ', '')) AS pos " +
		    "FROM book b " +
		    "LEFT JOIN ( " +
		    "    SELECT book_id FROM loan WHERE is_returned = 0 " +
		    ") l ON b.book_id = l.book_id " +
		    "WHERE REPLACE(b.title, ' ', '') LIKE CONCAT('%', REPLACE(?, ' ', ''), '%') " +
		    "AND REPLACE(b.publisher, ' ', '') LIKE CONCAT('%', REPLACE(?, ' ', ''), '%') " +
		    "ORDER BY pos ASC";

		try (Connection conn = DBUtil.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);     // for LOCATE
			pstmt.setString(2, title);     // for title LIKE
			pstmt.setString(3, publisher); // for publisher LIKE

	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            SearchedBook book = new SearchedBook();
	            book.setBookId(rs.getInt("BOOK_ID"));
	            book.setTitle(rs.getString("TITLE"));
	            book.setAuthor(rs.getString("AUTHOR"));
	            book.setPublisher(rs.getString("PUBLISHER"));
	            book.setIsbn(rs.getString("ISBN"));
	            book.setStatus(rs.getString("STATUS"));
	            books.add(book);
	        }
	    }
	    return books;
	}

	public RemoveResult deleteBook(int bookId) {
		String sql = "DELETE FROM BOOK WHERE book_id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
