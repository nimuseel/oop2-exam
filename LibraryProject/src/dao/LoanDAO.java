package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import util.DBUtil;
import util.InsertResult;

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

}
