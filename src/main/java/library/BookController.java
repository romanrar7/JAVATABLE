package library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class BookController {

    @FXML private TableView<Book> table;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, Integer> colCopies;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField yearField;
    @FXML private TextField copiesField;

    @FXML private TextField searchTitle;
    @FXML private TextField searchAuthor;

    @FXML private Button saveButton;

    private final BookService service = new BookService();
    private final ObservableList<Book> data = FXCollections.observableArrayList();
    private int selectedId = 0;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copies"));
        table.setItems(data);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldB, b) -> {
            if (b != null) {
                selectedId = b.getId();
                titleField.setText(b.getTitle());
                authorField.setText(b.getAuthor());
                yearField.setText(String.valueOf(b.getYear()));
                copiesField.setText(String.valueOf(b.getCopies()));
                saveButton.setText("Оновити");
            }
        });

        try {
            Database.init();
            loadAll();
        } catch (Exception e) {
            error("Не вдалося підключитися до бази. Перевірте db.properties і запустіть schema.sql.");
        }
    }

    @FXML
    private void onSearch() {
        try {
            data.setAll(service.search(searchTitle.getText(), searchAuthor.getText()));
            if (data.isEmpty()) {
                info("Нічого не знайдено");
            }
        } catch (SQLException e) {
            error(dbError(e));
        }
    }

    @FXML
    private void onShowAll() {
        searchTitle.clear();
        searchAuthor.clear();
        loadAll();
    }

    @FXML
    private void onNew() {
        table.getSelectionModel().clearSelection();
        selectedId = 0;
        clearForm();
        saveButton.setText("Додати");
    }

    @FXML
    private void onSave() {
        Book b = new Book();
        b.setId(selectedId);
        b.setTitle(titleField.getText());
        b.setAuthor(authorField.getText());
        try {
            b.setYear(Integer.parseInt(yearField.getText().trim()));
            b.setCopies(Integer.parseInt(copiesField.getText().trim()));
        } catch (NumberFormatException e) {
            error("Рік і кількість мають бути числами");
            return;
        }

        try {
            if (selectedId == 0) {
                service.add(b);
                info("Книгу додано");
            } else {
                service.update(b);
                info("Книгу оновлено");
            }
            loadAll();
            onNew();
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        } catch (SQLException e) {
            error(dbError(e));
        }
    }

    @FXML
    private void onDelete() {
        Book b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            error("Виберіть книгу у таблиці");
            return;
        }
        try {
            service.delete(b.getId());
            info("Книгу видалено");
            loadAll();
            onNew();
        } catch (SQLException e) {
            error(dbError(e));
        }
    }

    private void loadAll() {
        try {
            data.setAll(service.getAll());
        } catch (SQLException e) {
            error(dbError(e));
        }
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        yearField.clear();
        copiesField.clear();
    }

    private String dbError(SQLException e) {
        e.printStackTrace();
        String state = e.getSQLState();
        if (state != null && state.startsWith("08")) {
            return "Немає зв'язку з базою даних. Перевірте, чи запущений сервер БД.";
        }
        if (state != null && state.startsWith("23")) {
            return "Такі дані вже є або порушено обмеження таблиці.";
        }
        if (state != null && state.startsWith("42")) {
            return "Помилка запиту або немає таблиці books.";
        }
        return "Помилка бази даних: " + e.getMessage();
    }

    private void error(String text) {
        Alert a = new Alert(Alert.AlertType.ERROR, text);
        a.setHeaderText(null);
        a.setTitle("Помилка");
        a.showAndWait();
    }

    private void info(String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, text);
        a.setHeaderText(null);
        a.setTitle("Повідомлення");
        a.showAndWait();
    }
}
