package gui;

import java.awt.EventQueue;
import javax.swing.*;

import dao.UserDAO;
import util.InsertResult;

public class Signup extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField idField;
	private JPasswordField pwField;
	private JPasswordField pwConfirmField;
	private InsertResult ir = InsertResult.FAIL;
	private JTextField nameField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Signup dialog = new Signup(null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Signup(JFrame parent) {
		super(parent, "회원가입", true);
		setSize(400, 320);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(null);

		JLabel idLabel = new JLabel("아이디:");
		idLabel.setBounds(26, 27, 80, 40);
		getContentPane().add(idLabel);
		idField = new JTextField();
		idField.setBounds(120, 27, 229, 40);
		getContentPane().add(idField);

		JLabel pwLabel = new JLabel("비밀번호:");
		pwLabel.setBounds(26, 106, 80, 40);
		getContentPane().add(pwLabel);
		pwField = new JPasswordField();
		pwField.setBounds(120, 106, 229, 40);
		getContentPane().add(pwField);

		JLabel pwConfirmLabel = new JLabel("비밀번호 확인:");
		pwConfirmLabel.setBounds(26, 147, 100, 40);
		getContentPane().add(pwConfirmLabel);
		pwConfirmField = new JPasswordField();
		pwConfirmField.setBounds(120, 147, 229, 40);
		getContentPane().add(pwConfirmField);

		JButton registerBtn = new JButton("회원가입");
		registerBtn.setBounds(60, 210, 120, 40);
		JButton cancelBtn = new JButton("취소");
		cancelBtn.setBounds(210, 210, 120, 40);
		getContentPane().add(registerBtn);
		getContentPane().add(cancelBtn);

		JLabel nameLabel = new JLabel("이름:");
		nameLabel.setBounds(26, 66, 80, 40);
		getContentPane().add(nameLabel);

		nameField = new JTextField();
		nameField.setBounds(120, 66, 229, 40);
		getContentPane().add(nameField);

		registerBtn.addActionListener(e -> {
			String id = new String(idField.getText());
			String pw = new String(pwField.getPassword());
			String pwConfirm = new String(pwConfirmField.getPassword());
			String name = new String(nameField.getText());
			if (!isValidPassword(pw)) {
				JOptionPane.showMessageDialog(this, "비밀번호는 대소문자 각각 1개 이상,\n특수문자(!@#*_^ 중 하나) 1개 이상을 포함해야 합니다.",
						"비밀번호 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!pw.equals(pwConfirm)) {
				JOptionPane.showMessageDialog(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", "비밀번호 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			UserDAO userDAO = new UserDAO();
			ir = userDAO.insertUser(id, pw, name);
			
			if(ir == InsertResult.DUPLICATE_KEY) {
				JOptionPane.showMessageDialog(this, "중복된 아이디 입니다.", "중복된 아이디", JOptionPane.ERROR_MESSAGE);
				return;
			}else if(ir == InsertResult.FAIL) {
				JOptionPane.showMessageDialog(this, "예기치 못한 오류입니다.", "오류발생", JOptionPane.ERROR_MESSAGE);
				return;
			}
			setVisible(false);
		});
		cancelBtn.addActionListener(e -> setVisible(false));
	}

	// 비밀번호 유효성 검사
	private boolean isValidPassword(String pw) {
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasSpecial = false;
		String specialChars = "!@#*_^";
		for (char c : pw.toCharArray()) {
			if (Character.isUpperCase(c))
				hasUpper = true;
			else if (Character.isLowerCase(c))
				hasLower = true;
			else if (specialChars.indexOf(c) >= 0)
				hasSpecial = true;
		}
		return hasUpper && hasLower && hasSpecial;
	}

	public InsertResult isRegistered() {
		return ir;
	}

	public String getIdText() {
		return idField.getText();
	}

	public String getPasswordText() {
		return new String(pwField.getPassword());
	}

	public String getPasswordConfirmText() {
		return new String(pwConfirmField.getPassword());
	}
}
