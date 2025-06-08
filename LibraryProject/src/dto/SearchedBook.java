package dto;

public class SearchedBook {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String status;
    private int bookId;
    private String imageUrl;

    public SearchedBook(String title, String author, String publisher, String isbn,
                        String status, int bookId, String imageUrl) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.status = status;
        this.bookId = bookId;
        this.imageUrl = imageUrl;
    }

    public SearchedBook() {}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

    // Getters & Setters
    // ...
}
