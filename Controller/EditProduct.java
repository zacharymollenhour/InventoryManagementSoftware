package Controller;

import Models.Part;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EditProduct implements Initializable {

    //Initialize FXML Variables
    @FXML
    private TextField PartSearch;

    @FXML
    private TextField ProductID;

    @FXML
    private TextField ProductName;

    @FXML
    private TextField ProductStock;

    @FXML
    private TextField ProductPrice;

    @FXML
    private TextField ProductMax;

    @FXML
    private TextField ProductMin;

    @FXML
    private Label ViewTitle;

    @FXML
    private Label PartTablePlaceHolder;

    @FXML
    private TableView<Part> PartTableView, AssociatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> PartIDColumn, PartInventoryColumn, AssociatedPartIDColumn, AssociatedPartInventoryColumn;
    @FXML
    private TableColumn<Part, String> PartNameColumn, AssociatedPartNameColumn;
    @FXML
    private TableColumn<Part, Double> PartPriceColumn, AssociatedPartPriceColumn;

}
