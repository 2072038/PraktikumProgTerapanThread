package com.pterapan.demosql.controller;

import com.pterapan.demosql.dao.CategoryDao;
import com.pterapan.demosql.dao.ItemsDao;
import com.pterapan.demosql.model.Category;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    public Label labelIdCategory;
    public TextField txtIdCategory;
    public Label labelNameCategory;
    public TextField txtNameCategory;
    public Button btnSave;
    public TableView<Category> tableCategory;
    public TableColumn<Category, Integer> colId;
    public TableColumn<Category, String> colCatName;

    ObservableList<Category> clist_tampilan;

    private ItemsController itemsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCatName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void setItemsController(ItemsController itemsController) {
        this.itemsController = itemsController;
        tableCategory.setItems(itemsController.getClist());
    }

    public void addCategory(ActionEvent actionEvent) {
        CategoryDao dao = new CategoryDao();
        String id = txtIdCategory.getText();
        String name = txtNameCategory.getText();

        if (id != "" && name != "") {
            dao.addData(new Category(Integer.valueOf(id), name));
            clist_tampilan = dao.getData();
            tableCategory.setItems(clist_tampilan);

            txtIdCategory.clear();
            txtNameCategory.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
