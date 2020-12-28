package Controller;

import Models.Inventory;
import Models.Part;
import Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Core.Main.*;
import Core.DataValidation.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static Core.Main.LoadView;

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

    private ObservableList<Part> AssociatedParts;
    private int ProductIndex = -1;
    private Stage stage;
    private static final String NewProductText = "Automatically Generated";
    private static final String ModifyProductText = "Modify Product";


    public void SaveProductClick(ActionEvent actionEvent) throws IOException {
        //Create StagedPart
        Part stagedPart;


        ArrayList<String> ProductValidationErrors = new ArrayList<>();


        String productName = ProductName.getText();
        int productStock = Core.DataValidation.ValidateIntInput(ProductStock,  "Inv", ProductValidationErrors);
        double productPrice = Core.DataValidation.ValidateDoubleInput(ProductPrice, ProductValidationErrors, "Price");
        int productMax = Core.DataValidation.ValidateIntInput(ProductMax, "Max", ProductValidationErrors);
        int productId = ProductID.getText().equals(NewProductText) ? Inventory.getNextProductID() : Integer.parseInt(ProductID.getText());
        int productMin = Core.DataValidation.ValidateIntInput(ProductMin, "Min", ProductValidationErrors);
        if (productMin > productMax) {
            ProductValidationErrors.add("Min must be less than Max.");
        }
        if (productStock < productMin || productStock > productMax) {
            ProductValidationErrors.add("Inv must be greater than Min and less than Max.");
        }

        // Initialize the staged product and associate the parts from the controller.
        Product stagedProduct = new Product(productId, productName,productPrice,productStock,productMin,productMax);
        for (Part stagedAssociatedPart : AssociatedParts) {
            stagedProduct.addAssociatedPart(stagedAssociatedPart);
        }

        // Inform the user if any validation failed, and return to the edit screen if so.
        if (ProductValidationErrors.size() > 0) {
            Alert alert = Core.Main.AlertHelper(Alert.AlertType.ERROR, "One or more fields failed to validate. Please " +
                    "review the following and make the necessary corrections.", String.join("\n", ProductValidationErrors));
            Core.Main.AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        // Prepare and display a confirmation dialog showing (mostly) parsed values.
        StringBuilder confirmData = new StringBuilder(String.format("ID: %d\nName: %s\nInventory: %d\n" +
                "Unit Price: %.2f\nMax: %d\nMin: %d\nAssociated Parts:\n", productId, productName,productPrice,productStock,productMin,productMax));
        for (Part stagedAssociatedPart : AssociatedParts) {
            confirmData.append(String.format("\t%s\n", stagedAssociatedPart.getName()));
        }
        Alert alert = Core.Main.AlertHelper(Alert.AlertType.CONFIRMATION,
                "Please review the following details and confirm they are correct.", confirmData.toString());
        Core.Main.AlertDisplayHelper(alert);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        // Check if a productIndex was specified for an existing product we're editing, and update that particular
        // product in the inventory, otherwise create a new one, and then return to the main screen.
        if (ProductIndex >= 0) {
            Inventory.updateProduct(ProductIndex, stagedProduct);
        } else {
            Inventory.addProduct(stagedProduct);
        }

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        LoadView(stage, "/views/Main.fxml");
    }





    public void productEdit(int SelectedIndex, Product SelectedProduct) {
        ViewTitle.setText(ModifyProductText);
        ProductMin.setText(String.valueOf(SelectedProduct.getMin()));
        ProductMax.setText(String.valueOf(SelectedProduct.getMax()));
        ProductID.setText(String.valueOf(SelectedProduct.getID()));
        ProductStock.setText(String.valueOf(SelectedProduct.getStock()));
        ProductPrice.setText(String.valueOf(SelectedProduct.getPrice()));
        ProductName.setText(String.valueOf(SelectedProduct.getName()));
        AssociatedParts.setAll(SelectedProduct.getAllAssociatedParts());
        AssociatedPartTableView.setItems(AssociatedParts);
        ProductIndex = SelectedIndex;
    }

    public void AssociatePartToProduct(ActionEvent actionEvent) {
        if (PartTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "A part must be selected in order to add one.");
            Core.Main.AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }
        AssociatedParts.add(PartTableView.getSelectionModel().getSelectedItem());
        AssociatedPartTableView.setItems(AssociatedParts);
    }

    public void DissociatePartFromProduct(ActionEvent actionEvent) {
        if (AssociatedPartTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, " A part must be selected to remove it from the product.");
            Core.Main.AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, " Are you sure you want to remove this part from the product?.");
            Core.Main.AlertDisplayHelper(alert);
            Optional<ButtonType> DissociateResult = alert.showAndWait();
            if (DissociateResult.isEmpty() || DissociateResult.get() != ButtonType.OK) {
                return;
            }

            AssociatedParts.remove(AssociatedPartTableView.getSelectionModel().getSelectedItem());
            AssociatedPartTableView.setItems(AssociatedParts);
        }
    }

    public void CancelProductClick(ActionEvent actionEvent) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will be discarded. Is this OK?");
        Core.Main.AlertDisplayHelper(alert);
        Optional<ButtonType> CancelResult = alert.showAndWait();
        if (CancelResult.isEmpty() || CancelResult.get() != ButtonType.OK) {
            return;
        }

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        LoadView(stage, "/views/Main.fxml");
    }
    public void startAdd() {
        ViewTitle.setText("Add Product");
        ProductID.setText(NewProductText);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartTableView.setItems(Inventory.getAllParts());
        PartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssociatedParts = FXCollections.observableArrayList();
    }
}
