package dto;

public class SearchedBook extends Book {
	String status;

	public SearchedBook(String title, String author, String publisher, String isbn, String status, int bookId,
			String imageUrl) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.isbn = isbn;
		this.status = status;
		this.bookId = bookId;
		this.imageUrl = imageUrl;
		// TODO Auto-generated constructor stub
	}

	public SearchedBook() {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
