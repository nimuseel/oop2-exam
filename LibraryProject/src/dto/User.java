package dto;

public class User {
	private String userId;
	private String password;
	private String userName;
	private boolean isAdmin;
	
	public User(String userId, String password, String userName, boolean isAdmin) {
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.isAdmin = isAdmin;
		// TODO Auto-generated constructor stub
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
