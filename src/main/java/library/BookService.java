package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public List<Book> getAll() throws SQLException {
        String sql = "SELECT * FROM books ORDER BY book_id";
        List<Book> list = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(read(rs));
            }
        }
        return list;
    }

    public List<Book> search(String title, String author) throws SQLException {
        String sql = "SELECT * FROM books WHERE title LIKE ? AND author LIKE ? ORDER BY book_id";
        List<Book> list = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + title.trim() + "%");
            st.setString(2, "%" + author.trim() + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(read(rs));
                }
            }
        }
        return list;
    }

    public void add(Book b) throws SQLException {
        b.check();
        String sql = "INSERT INTO books (title, author, publish_year, copies) VALUES (?, ?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, b.getTitle().trim());
            st.setString(2, b.getAuthor().trim());
            st.setInt(3, b.getYear());
            st.setInt(4, b.getCopies());
            st.executeUpdate();
        }
    }

    public int update(Book b) throws SQLException {
        b.check();
        String sql = "UPDATE books SET title = ?, author = ?, publish_year = ?, copies = ? WHERE book_id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, b.getTitle().trim());
            st.setString(2, b.getAuthor().trim());
            st.setInt(3, b.getYear());
            st.setInt(4, b.getCopies());
            st.setInt(5, b.getId());
            return st.executeUpdate();
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate();
        }
    }

    private Book read(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("book_id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setYear(rs.getInt("publish_year"));
        b.setCopies(rs.getInt("copies"));
        return b;
    }
}
