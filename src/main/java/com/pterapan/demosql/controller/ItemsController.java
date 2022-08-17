package com.pterapan.demosql.controller;

import com.pterapan.demosql.ThreadGroup;
import com.pterapan.demosql.ThreadSimple;
import com.pterapan.demosql.dao.CategoryDao;
import com.pterapan.demosql.dao.ItemsDao;
import com.pterapan.demosql.model.Category;
import com.pterapan.demosql.model.Items;
import com.pterapan.demosql.HelloApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemsController implements Initializable {
    public MenuItem labelMenu;
    public Label labelId;
    public TextField txtIdItems;
    public Label labelName;
    public TextField txtNameItems;
    public Label labelPrice;
    public TextField txtPrice;
    public Label labelDescription;
    public TextArea txtDescription;
    public ComboBox<Category> cmb1;
    public TableView<Items> tableItems;
    public TableColumn<Items, Integer> colId;
    public TableColumn<Items, String> colName;
    public TableColumn<Items, Double> colPrice;
    public TableColumn<Items, Category> colCategory;
    public Button btnUpdate;
    public Button bnSave;
    public Button btnReset;
    public Button btnDelete;
    public MenuItem labelSimple;
    public MenuItem labelGroup;

    private ObservableList<Items> ilist;
    private ObservableList<Category> clist;
    private ItemsDao itemsDao;
    private CategoryDao categoryDao;

    private Items selectedItems;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        itemsDao = new ItemsDao();
        categoryDao = new CategoryDao();
        ilist = FXCollections.observableArrayList();
        clist = FXCollections.observableArrayList();

        ilist.addAll(itemsDao.getData());
        clist.addAll(categoryDao.getData());

        cmb1.setItems(clist);
        tableItems.setItems(ilist);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCategory()));

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        labelMenu.setAccelerator(KeyCombination.keyCombination("Alt+F2"));
        labelSimple.setAccelerator(KeyCombination.keyCombination("Alt+S"));
        labelGroup.setAccelerator(KeyCombination.keyCombination("Alt+G"));
    }

    public void refreshData() {
        ItemsDao dao = new ItemsDao();
        ilist = dao.getData();
        tableItems.setItems(ilist);
    }

    public void goToCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader;
        loader = new FXMLLoader(HelloApplication.class.getResource("second_category.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        CategoryController cController = loader.getController();
        cController.setItemsController(this);
        stage.setTitle("Category Management");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void addItems(ActionEvent actionEvent) {
        ItemsDao dao = new ItemsDao();
        String id = txtIdItems.getText();
        String name = txtNameItems.getText();
        String price = txtPrice.getText();
        String description = txtDescription.getText();
        Category category = cmb1.getValue();

        if (id != "" && name != "" && price != "" && description != "") {
            dao.addData(new Items(Integer.valueOf(id), name, Double.valueOf(price), description, category));
            ilist = dao.getData();
            tableItems.setItems(ilist);

            txtIdItems.clear();
            txtNameItems.clear();
            txtPrice.clear();
            txtDescription.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void resetItems() {
        txtIdItems.clear();
        txtNameItems.clear();
        txtPrice.clear();
        txtDescription.clear();
        selectedItems = null;
        bnSave.setDisable(false);
        btnReset.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public ObservableList<Category> getClist() {
        return clist;
    }

    public void delItems(ActionEvent actionEvent) {
        Items selectedItems;
        selectedItems = tableItems.getSelectionModel().getSelectedItem();

        ItemsDao dao = new ItemsDao();
        int hasil = dao.delData(selectedItems);
        if (hasil > 0) {
            Alert alertbox = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus");
            alertbox.showAndWait();
        }
        refreshData();
        txtIdItems.clear();
        txtNameItems.clear();
        txtPrice.clear();
        txtDescription.clear();
        bnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public void updateItems(ActionEvent actionEvent) {
        if (txtNameItems.getText().trim().isEmpty() || txtPrice.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty() || cmb1 == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        } else {
            selectedItems.setName(txtNameItems.getText().trim());
            selectedItems.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            selectedItems.setDescription(txtDescription.getText().trim());
            selectedItems.setCategory(cmb1.getValue());

            if (itemsDao.updateData(selectedItems) == 1) {
                ilist.clear();
                ilist.addAll(itemsDao.getData());

                txtIdItems.clear();
                txtNameItems.clear();
                txtPrice.clear();
                txtDescription.clear();
                bnSave.setDisable(false);
                btnReset.setDisable(false);
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
            }
        }
    }

    public void getSelectedItems(MouseEvent mouseEvent) {
        selectedItems = tableItems.getSelectionModel().getSelectedItem();
        if (selectedItems != null) {
            txtIdItems.setText(String.valueOf(selectedItems.getId()));
            txtNameItems.setText(selectedItems.getName());
            txtPrice.setText(String.valueOf(selectedItems.getPrice()));
            txtDescription.setText(selectedItems.getDescription());
            cmb1.setValue(selectedItems.getCategory());
            bnSave.setDisable(true);
            btnReset.setDisable(false);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void reset(ActionEvent actionEvent) {
        bnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        txtIdItems.clear();
        txtNameItems.clear();
        txtPrice.clear();
        txtDescription.clear();
    }

    public void lihatSimpleReport(ActionEvent actionEvent) {
        ThreadSimple simple = new ThreadSimple();
        ExecutorService exService = Executors.newCachedThreadPool();
        exService.execute(simple);
        exService.shutdown();
    }

    public void lihatGroupReport(ActionEvent actionEvent) {
        ThreadGroup group = new ThreadGroup();
        ExecutorService exService = Executors.newCachedThreadPool();
        exService.execute(group);
        exService.shutdown();
    }
}
