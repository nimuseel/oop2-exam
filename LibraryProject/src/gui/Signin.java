package gui;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import dao.UserDAO;
import dto.User;
import util.SessionContext;

public class Signin extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField idField;
    private JPasswordField pwField;
 

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	Signin dialog = new Signin(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Signin(JFrame parent) {
        super(parent, "로그인", true);
        setSize(400, 220);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);

        JLabel idLabel = new JLabel("아이디:");
        idLabel.setBounds(26, 30, 80, 40);
        getContentPane().add(idLabel);
        idField = new JTextField();
        idField.setBounds(120, 30, 229, 40);
        getContentPane().add(idField);

        JLabel pwLabel = new JLabel("비밀번호:");
        pwLabel.setBounds(26, 90, 80, 40);
        getContentPane().add(pwLabel);
        pwField = new JPasswordField();
        pwField.setBounds(120, 90, 229, 40);
        pwField.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyTyped(KeyEvent e) {
		        char c = e.getKeyChar();
		        // 영어 대소문자만 허용
		        if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
		            e.consume(); // 입력 무시
		        }
		    }
		});
        getContentPane().add(pwField);

        JButton loginBtn = new JButton("로그인");
        loginBtn.setBounds(97, 142, 120, 40);
        JButton signupBtn = new JButton("회원가입");
        signupBtn.setBounds(229, 142, 120, 40);
        getContentPane().add(loginBtn);
        getContentPane().add(signupBtn);

        loginBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String id = new String(idField.getText());
        		String pw = new String(pwField.getPassword());
        		
        		System.out.println(id+" / "+ pw);
        		
        		UserDAO userDAO = new UserDAO();
        		User user = userDAO.loginUser(id, pw);
        		if (user != null) {
        		    LibraryMenu lm = new LibraryMenu(null, user.isAdmin());
        		    SessionContext.getInstance().setCurrentUserId(id);
        		    setVisible(false);	
        		    lm.setVisible(true);
        		    lm.setAlwaysOnTop(true);
        		} else {
        		    JOptionPane.showMessageDialog(null, "ID 또는 비밀번호가 잘못되었습니다.");
        		}
        	}
        });
        signupBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Signup signup = new Signup(null);
        		setVisible(false);
        		signup.setVisible(true);
        	}
        });
    }

    public String getIdText() {
        return idField.getText();
    }

    public String getPasswordText() {
        return new String(pwField.getPassword());
    }
}
