package gui;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CreateBook extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JTextField titleField; 
	private JTextField authorField;
	private JTextField publisherField;
	private JTextField isbnField;
    private boolean saved = false;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateBook dialog = new CreateBook(null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public CreateBook(JFrame parent) {
        super(parent, "도서 정보 입력", true);
        setSize(468, 376);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);

        JLabel bookName = new JLabel("제목:");
        bookName.setBounds(26, 1, 59, 61);
        getContentPane().add(bookName);
        JTextField titleField = new JTextField("");
        titleField.setBounds(169, 1, 229, 61);
        getContentPane().add(titleField);

        JLabel author = new JLabel("저자:");
        author.setBounds(26, 74, 59, 61);
        getContentPane().add(author);
        JTextField authorField = new JTextField("");
        authorField.setBounds(169, 74, 229, 61);
        getContentPane().add(authorField);

        JLabel publisher = new JLabel("출판사:");
        publisher.setBounds(26, 145, 59, 61);
        getContentPane().add(publisher);
        JTextField publisherField = new JTextField("");
        publisherField.setBounds(169, 145, 229, 61);
        getContentPane().add(publisherField);

        JLabel isbn = new JLabel("ISBN:");
        isbn.setBounds(26, 214, 59, 61);
        getContentPane().add(isbn);
        JTextField isbnField = new JTextField("");
        isbnField.setBounds(169, 214, 229, 61);
        getContentPane().add(isbnField);

        JButton saveBtn = new JButton("저장");
        saveBtn.setBounds(47, 285, 182, 61);
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(239, 285, 182, 61);
        getContentPane().add(saveBtn);
        getContentPane().add(cancelBtn);

        saveBtn.addActionListener(e -> {
            saved = true;
            setVisible(false);
        });
        cancelBtn.addActionListener(e -> setVisible(false));
    }

}
