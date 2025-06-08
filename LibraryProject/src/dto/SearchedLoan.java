package dto;

import java.util.Date;

public class SearchedLoan {

	private int loanId;
	private String title;
	private String author;
	private String publisher;
	private String isbn;
	private Date loanDate;
	private Date dueDate;
	private boolean isReturned;
	private int bookId;

	public SearchedLoan(int loanId, String title, String author, String publisher, String isbn, Date loanDate,
			Date dueDate, boolean isReturned, int bookId) {
		this.loanId = loanId;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.isbn = isbn;
		this.loanDate = loanDate;
		this.dueDate = dueDate;
		this.isReturned = isReturned;
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isReturned() {
		return isReturned;
	}

	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

}
