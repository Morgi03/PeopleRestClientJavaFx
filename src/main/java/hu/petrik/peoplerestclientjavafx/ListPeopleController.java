package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.binding.StringExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;

public class ListPeopleController {

    @FXML
    private Button insertButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private TableView<Person> peopleTable;
    @FXML
    private TableColumn<Person, String> nameCol;
    @FXML
    private TableColumn<Person, String> emailCol;
    @FXML
    private TableColumn<Person, Integer> ageCol;

    @FXML
    private void initialize() {
        // Ha így írjuk be akkor getName() függvény eredményét írja ki.
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        Platform.runLater(() -> {
            try {
                Response response = RequestHandler.get(App.BASE_URL);
                String content = response.getContent();
                Gson converter = new Gson();
                Person[] people = converter.fromJson(content, Person[].class);
                for (Person person : people) {
                    peopleTable.getItems().add(person);
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Couldn't get data form the server");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Platform.exit();

            }
        });
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        int ItemIndex = peopleTable.getSelectionModel().getSelectedIndex();
        if (ItemIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Figyelmeztetés");
            alert.setContentText("Törléshez előbb válasszon ki elemet");
            alert.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SURE?");
        alert.setContentText("Biztos benne, hogy törli a kiválasztott személyt?");
        alert.setHeaderText("MEGERŐSÍTÉS");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get().equals(ButtonType.OK)){
            Person torlendoEmber = peopleTable.getSelectionModel().getSelectedItem();
            String personToJson = String.format("{\"name\": \"%s\", \"email\": \"%s\", \"age\": \"%s\"}", torlendoEmber.getName(), torlendoEmber.getEmail(), torlendoEmber.getAge());
            peopleTable.getItems().remove(torlendoEmber);
            //TODO: törlés API-ból
        }

    }
}