package library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

    @Test
    void normalBookIsOk() {
        Book b = new Book(1, "Кобзар", "Шевченко", 1840, 3);
        assertDoesNotThrow(b::check);
    }

    @Test
    void emptyTitleFails() {
        Book b = new Book(0, "", "Автор", 2000, 1);
        assertThrows(IllegalArgumentException.class, b::check);
    }

    @Test
    void negativeCopiesFails() {
        Book b = new Book(0, "Назва", "Автор", 2000, -1);
        assertThrows(IllegalArgumentException.class, b::check);
    }
}
