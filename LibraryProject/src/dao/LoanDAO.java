package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dto.SearchedLoan;
import util.DBUtil;
import util.InsertResult;
import util.SessionContext;

public class LoanDAO {
	public InsertResult borrowBook(int bookId, String userId) {
		String sql = "INSERT INTO loan (book_id, user_id, loan_date, due_date, is_returned) " +
	             "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), FALSE)";

		try (Connection conn = DBUtil.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {

		    pstmt.setInt(1, bookId);
		    pstmt.setString(2, userId); // 로그인한 사용자 ID

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
	public List<SearchedLoan> searchLoan(){
		List<SearchedLoan> loanList = new ArrayList<>();
		String sql = 
			    "SELECT b.title, b.author, b.publisher, b.isbn, " +
			    "       l.loan_id, l.loan_date, l.due_date, l.is_returned, l.book_id " +
			    "FROM loan l " +
			    "JOIN book b ON l.book_id = b.book_id " +
			    "WHERE l.user_id = ? " +
			    "ORDER BY l.is_returned ASC, l.loan_date DESC";

		try (Connection conn = DBUtil.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {

		    pstmt.setString(1, SessionContext.getInstance().getCurrentUserId());
		    ResultSet rs = pstmt.executeQuery();

		    while (rs.next()) {
		    	int loanId = rs.getInt("loan_id");
		        String title = rs.getString("title");
		        String author = rs.getString("author");
		        String publisher = rs.getString("publisher");
		        String isbn = rs.getString("isbn");
		        Date loanDate = rs.getDate("loan_date");
		        Date dueDate = rs.getDate("due_date");
		        boolean isReturned = rs.getBoolean("is_returned");
		        int bookId = rs.getInt("book_id");

		        SearchedLoan loan = new SearchedLoan(loanId, title, author, publisher, isbn,
		                                             loanDate, dueDate, isReturned, bookId);
		        loanList.add(loan);
		    }

		} catch (SQLException e) {
		    e.printStackTrace();
		}

		return  loanList;
		
	}
	
	public List<SearchedLoan> searchLoansByTitle(String titleKeyword) {
	    List<SearchedLoan> loanList = new ArrayList<>();

	    String sql = 
	    	    "SELECT b.title, b.author, b.publisher, b.isbn, " +
	    	    "       l.loan_id, l.loan_date, l.due_date, l.is_returned, l.book_id " +
	    	    "FROM loan l " +
	    	    "JOIN book b ON l.book_id = b.book_id " +
	    	    "WHERE l.user_id = ? " +
	    	    "  AND REPLACE(LOWER(b.title), ' ', '') LIKE CONCAT('%', REPLACE(LOWER(?), ' ', ''), '%') " +
	    	    "ORDER BY b.title ASC";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	    	pstmt.setString(1, SessionContext.getInstance().getCurrentUserId());
	        pstmt.setString(2, titleKeyword);

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	        	int loanId = rs.getInt("loan_id");
		        String title = rs.getString("title");
		        String author = rs.getString("author");
		        String publisher = rs.getString("publisher");
		        String isbn = rs.getString("isbn");
		        Date loanDate = rs.getDate("loan_date");
		        Date dueDate = rs.getDate("due_date");
		        boolean isReturned = rs.getBoolean("is_returned");
		        int bookId = rs.getInt("book_id");

		        SearchedLoan loan = new SearchedLoan(loanId, title, author, publisher, isbn,
		                                             loanDate, dueDate, isReturned, bookId);

		        loanList.add(loan);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return loanList;
	}
	
	public List<SearchedLoan> searchLoansByPublisher(String publisherKeyword) {
	    List<SearchedLoan> loanList = new ArrayList<>();

	    String sql =
	        "SELECT b.title, b.author, b.publisher, b.isbn, " +
	        "       l.loan_id, l.loan_date, l.due_date, l.is_returned, l.book_id " +
	        "FROM loan l " +
	        "JOIN book b ON l.book_id = b.book_id " +
	        "WHERE l.user_id = ? " +
	        "  AND REPLACE(LOWER(b.publisher), ' ', '') LIKE CONCAT('%', REPLACE(LOWER(?), ' ', ''), '%') " +
	        "ORDER BY b.title ASC";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	    	pstmt.setString(1, SessionContext.getInstance().getCurrentUserId());
	        pstmt.setString(2, publisherKeyword);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	            	int loanId = rs.getInt("loan_id");
			        String title = rs.getString("title");
			        String author = rs.getString("author");
			        String publisher = rs.getString("publisher");
			        String isbn = rs.getString("isbn");
			        Date loanDate = rs.getDate("loan_date");
			        Date dueDate = rs.getDate("due_date");
			        boolean isReturned = rs.getBoolean("is_returned");
			        int bookId = rs.getInt("book_id");

			        SearchedLoan loan = new SearchedLoan(loanId, title, author, publisher, isbn,
			                                             loanDate, dueDate, isReturned, bookId);

			        loanList.add(loan);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return loanList;
	}
	
	public List<SearchedLoan> searchLoansByTitleAndPublisher(String titleKeyword, String publisherKeyword) {
	    List<SearchedLoan> loanList = new ArrayList<>();

	    String sql =
	        "SELECT b.title, b.author, b.publisher, b.isbn, " +
	        "       l.loan_id, l.loan_date, l.due_date, l.is_returned, l.book_id " +
	        "FROM loan l " +
	        "JOIN book b ON l.book_id = b.book_id " +
	        "WHERE l.user_id = ? " +
	        "  AND REPLACE(LOWER(b.title), ' ', '') LIKE CONCAT('%', REPLACE(LOWER(?), ' ', ''), '%') " +
	        "  AND REPLACE(LOWER(b.publisher), ' ', '') LIKE CONCAT('%', REPLACE(LOWER(?), ' ', ''), '%') " +
	        "ORDER BY b.title ASC";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	    	pstmt.setString(1, SessionContext.getInstance().getCurrentUserId());
	        pstmt.setString(2, titleKeyword);
	        pstmt.setString(3, publisherKeyword);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	            	int loanId = rs.getInt("loan_id");
			        String title = rs.getString("title");
			        String author = rs.getString("author");
			        String publisher = rs.getString("publisher");
			        String isbn = rs.getString("isbn");
			        Date loanDate = rs.getDate("loan_date");
			        Date dueDate = rs.getDate("due_date");
			        boolean isReturned = rs.getBoolean("is_returned");
			        int bookId = rs.getInt("book_id");

			        SearchedLoan loan = new SearchedLoan(loanId, title, author, publisher, isbn,
			                                             loanDate, dueDate, isReturned, bookId);

			        loanList.add(loan);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return loanList;
	}


	public boolean returnBook(int loanId) {
	    String sql = "UPDATE loan SET is_returned = true, return_date = CURRENT_DATE WHERE loan_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, loanId);
	        int result = pstmt.executeUpdate();
	        return result > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
