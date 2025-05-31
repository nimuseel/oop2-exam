package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import util.DBUtil;
import util.InsertResult;

public class UserDAO {
	public  InsertResult insertUser(String id, String pw, String name) {
		String sql = "INSERT INTO USER (ID, PASSWORD, NAME) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, name);
            
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
}
