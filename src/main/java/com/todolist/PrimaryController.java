package com.todolist;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Vector;

import MainCode.File_Reader;
import MainCode.File_Writer;
import MainCode.ToDoList;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {

    @FXML
    private Label error;

    @FXML
    private ImageView errorimg;

    @FXML
    private Button history, completed, markComplete, updateTask, removeTask;

    @FXML
    private TableColumn<ToDoList, String> time1;

    @FXML
    private TableColumn<ToDoList, String> completed1;

    @FXML
    private TableColumn<ToDoList, String> subject1;

    @FXML
    private TableColumn<ToDoList, String> location1;

    @FXML
    private TableView<ToDoList> currentTable;

    @FXML
    private TableColumn<ToDoList, String> date1;

    @FXML
    private TableColumn<ToDoList, String> description1;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        error.setVisible(false);
        errorimg.setVisible(false);
        try {
            App.tasks = File_Reader.checkFileForData();
        } catch (ParseException e1) {

        }

        addObservableListToTable(App.tasks);

        markComplete.setOnMouseEntered(event1 -> {
            if (currentTable.getSelectionModel().getSelectedItem() == null) {
                error.setVisible(true);
                errorimg.setVisible(true);
            }
        });

        markComplete.setOnMouseExited(event2 -> {
            error.setVisible(false);
            errorimg.setVisible(false);
        });

        updateTask.setOnMouseEntered(event1 -> {
            if (currentTable.getSelectionModel().getSelectedItem() == null) {
                error.setVisible(true);
                errorimg.setVisible(true);
            }
        });

        updateTask.setOnMouseExited(event2 -> {
            error.setVisible(false);
            errorimg.setVisible(false);
        });

        removeTask.setOnMouseEntered(event1 -> {
            if (currentTable.getSelectionModel().getSelectedItem() == null) {
                error.setVisible(true);
                errorimg.setVisible(true);
            }
        });

        removeTask.setOnMouseExited(event2 -> {
            error.setVisible(false);
            errorimg.setVisible(false);
        });

    }

    public void addObservableListToTable(Vector<ToDoList> tasks) {
        ObservableList<ToDoList> observableList = FXCollections.observableArrayList(tasks);

        subject1.setCellValueFactory(new PropertyValueFactory<ToDoList, String>("subject"));
        description1.setCellValueFactory(new PropertyValueFactory<ToDoList, String>("description"));
        location1.setCellValueFactory(new PropertyValueFactory<ToDoList, String>("location"));
        date1.setCellValueFactory(ToDoList -> {
            try {
                return new ReadOnlyStringWrapper(App.newDateFormat.format(
                        App.newDateFormat
                                .parse(App.newDateFormat.format(App.dateFormat.parse(ToDoList.getValue().getDate())))));
            } catch (ParseException e) {
                return new ReadOnlyStringWrapper("Error");
            }
        });
        time1.setCellValueFactory(ToDoList -> {
            try {
                return new ReadOnlyStringWrapper(App.newTimeForamt.format(
                        App.newTimeForamt
                                .parse(App.newTimeForamt.format(App.timeFormat.parse(ToDoList.getValue().getTime())))));
            } catch (ParseException e) {
                return new ReadOnlyStringWrapper("Error");
            }
        });
        completed1.setCellValueFactory(ToDoList -> {
            String s;
            if (Boolean.valueOf(ToDoList.getValue().getIsDoneValue())) {
                s = "Completed";
            } else
                s = "Not Completed";
            return new ReadOnlyStringWrapper(s);

        });

        currentTable.setItems(observableList);
    }

    @FXML
    public void searhTask() throws IOException {
        App.setRoot("searchTask");
    }

    @FXML
    public void emailList() throws IOException {
        App.email();
    }

    @FXML
    public void showHistory() throws IOException {
        App.setRoot("history");
    }

    @FXML
    public void showCompletedTask() throws IOException {
        App.setRoot("completed");
    }

    @FXML
    public void addTask(ActionEvent event) throws IOException {

        App.setRoot("addTask");
    }

    @FXML
    public void removeTask() {
        try {
            int index = currentTable.getSelectionModel().selectedIndexProperty().get();
            App.tasks.remove(index);
            App.tasks = ToDoList.sortSchedule(App.tasks);
            File_Writer.saveToFile(App.tasks);
            File_Writer.saveData(App.tasks);
            addObservableListToTable(App.tasks);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @FXML
    public void markTaskDone() {
        try {
            int index = currentTable.getSelectionModel().selectedIndexProperty().get();
            App.tasks.elementAt(index).setTaskAsDone();
            App.tasks = ToDoList.sortSchedule(App.tasks);
            File_Writer.saveToFile(App.tasks);
            File_Writer.saveData(App.tasks);
            addObservableListToTable(App.tasks);
            currentTable.refresh();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    static int index;

    @FXML
    public void updateTask(ActionEvent event){
        try {
            index = currentTable.getSelectionModel().selectedIndexProperty().get();
            App.setRoot("updateTask");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
