package library;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {

    private static String url;
    private static String user;
    private static String password;

    public static void init() throws IOException, SQLException {
        Properties props = new Properties();
        InputStream in = Database.class.getClassLoader().getResourceAsStream("db.properties");
        if (in == null) {
            throw new IOException("Не знайдено файл db.properties у resources");
        }
        props.load(in);
        in.close();

        url = props.getProperty("jdbc.url");
        user = props.getProperty("jdbc.user");
        password = props.getProperty("jdbc.password");

        createTable();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static void createTable() throws SQLException {
        String create = "CREATE TABLE IF NOT EXISTS books ("
                + "book_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "title VARCHAR(200) NOT NULL,"
                + "author VARCHAR(150) NOT NULL,"
                + "publish_year INT NOT NULL,"
                + "copies INT NOT NULL DEFAULT 1)";

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.execute(create);

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM books");
            rs.next();
            if (rs.getInt(1) == 0) {
                st.execute("INSERT INTO books (title, author, publish_year, copies) VALUES "
                        + "('Кобзар', 'Тарас Шевченко', 1840, 5),"
                        + "('Енеїда', 'Іван Котляревський', 1798, 3),"
                        + "('Лісова пісня', 'Леся Українка', 1911, 2)");
            }
        }
    }
}
