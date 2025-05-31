package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import dto.User;
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

	public User loginUser(String userId, String password) {
		String sql = "SELECT * FROM USER WHERE ID = ? AND PASSWORD = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            
            
            try (ResultSet rs = pstmt.executeQuery()) {
            	if(rs.next()) {
            		return new User(rs.getString("ID"),
            				rs.getString("PASSWORD"),
            				rs.getString("NAME"),
            				rs.getBoolean("IS_ADMIN")); // 결과가 존재하면 로그인 성공
            	}
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}
}
