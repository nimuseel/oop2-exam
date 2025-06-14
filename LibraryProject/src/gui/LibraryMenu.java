package gui;
import javax.swing.*;

import util.SessionContext;

public class LibraryMenu extends JDialog {
    private static final long serialVersionUID = 1L;

    public LibraryMenu(JFrame parent, String userId, boolean isAdmin) {
        super(parent, "메뉴", true);
        setSize(400, isAdmin ? 450 : 350);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);

        JButton borrowBtn = new JButton("도서 대출");
        borrowBtn.setBounds(120, 50, 150, 40);
        getContentPane().add(borrowBtn);

        JButton returnBtn = new JButton("도서 반납");
        returnBtn.setBounds(120, 100, 150, 40);
        getContentPane().add(returnBtn);

        JButton searchBtn = new JButton("도서 조회");
        searchBtn.setBounds(120, 150, 150, 40);
        getContentPane().add(searchBtn);

        // 로그아웃 버튼 (어드민/일반 모두)
        JButton logoutBtn = new JButton("로그아웃");
        if (isAdmin) {
            logoutBtn.setBounds(120, 330, 150, 40);
        } else {
            logoutBtn.setBounds(120, 230, 150, 40);
        }
        getContentPane().add(logoutBtn);
        
        JLabel lblWelcome = new JLabel(userId + "님," +  " 환영합니다!");
        lblWelcome.setBounds(6, 22, 388, 16);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblWelcome);

        logoutBtn.addActionListener(e -> {
            // 로그아웃 처리 후 다이얼로그 닫기
            SessionContext.getInstance().clear();
            Main main = new Main();
            dispose();
            main.setVisible(true);
            // 필요하다면 여기서 메인화면(로그인 등) 호출
        });

        if (isAdmin) {
            JButton addBtn = new JButton("도서 추가");
            addBtn.setBounds(120, 200, 150, 40);
            getContentPane().add(addBtn);

            addBtn.addActionListener(e -> {
                CreateBook createBook = new CreateBook(null);
                createBook.setAlwaysOnTop(true);
                createBook.setVisible(true);
                createBook.setModal(true);
            });

            JButton deleteBtn = new JButton("도서 삭제");
            deleteBtn.setBounds(120, 250, 150, 40);
            getContentPane().add(deleteBtn);

            deleteBtn.addActionListener(e -> {
                DeleteBook deleteBook = new DeleteBook(null);
                deleteBook.setAlwaysOnTop(true);
                deleteBook.setVisible(true);
                deleteBook.setModal(true);
            });

            JButton closeBtn = new JButton("닫기");
            closeBtn.setBounds(120, 380, 150, 40);
            getContentPane().add(closeBtn);

            closeBtn.addActionListener(e -> dispose());
        } else {
            JButton closeBtn = new JButton("닫기");
            closeBtn.setBounds(120, 280, 150, 40);
            getContentPane().add(closeBtn);

            closeBtn.addActionListener(e -> dispose());
        }

        borrowBtn.addActionListener(e -> {
            BorrowBook borrowBook = new BorrowBook(null);
            borrowBook.setAlwaysOnTop(true);
            borrowBook.setVisible(true);
            borrowBook.setModal(true);
        });

        returnBtn.addActionListener(e -> {
            ReturnBook returnBook = new ReturnBook(null);
            returnBook.setAlwaysOnTop(true);
            returnBook.setVisible(true);
            returnBook.setModal(true);
        });

        searchBtn.addActionListener(e -> {
            SearchBook searchBook = new SearchBook(null);
            searchBook.setAlwaysOnTop(true);
            searchBook.setVisible(true);
            searchBook.setModal(true);
        });
    }
}
