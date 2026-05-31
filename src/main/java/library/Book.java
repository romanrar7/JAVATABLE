package library;

public class Book {

    private int id;
    private String title;
    private String author;
    private int year;
    private int copies;

    public Book() {
    }

    public Book(int id, String title, String author, int year, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.copies = copies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public void check() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Введіть назву книги");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Введіть автора");
        }
        if (year < 1000 || year > 2100) {
            throw new IllegalArgumentException("Рік має бути від 1000 до 2100");
        }
        if (copies < 0) {
            throw new IllegalArgumentException("Кількість не може бути менше нуля");
        }
    }
}
