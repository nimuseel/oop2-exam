package util;
public class BookInterface {
    public String title, author, publisher, isbn, status;

    public BookInterface(String title, String author, String publisher, String isbn, String status) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.status = status;
    }

    public String toCSV() {
        return String.join(",", title, author, publisher, isbn, status);
    }

    public static BookInterface fromCSV(String line) {
        String[] arr = line.split(",", -1);
        if (arr.length < 5) return null;
        return new BookInterface(arr[0], arr[1], arr[2], arr[3], arr[4]);
    }
}
