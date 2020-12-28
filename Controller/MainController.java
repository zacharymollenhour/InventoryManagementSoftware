package Controller;
import Models.Inventory;
import Models.Product;
import javafx.fxml.FXML;
import Models.Part;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import Core.Main.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Core.Main.AlertDisplayHelper;
import static Core.Main.LoadView;

/**
 * Main Screen Controller
 *
 * @author Zachary Mollenhour
 */
public class MainController implements Initializable {
    //Initialize JavaFX Instance Values
    Stage stage;
    @FXML
    private TableColumn <Product, Double> ProductPriceColumn;
    @FXML
    private TableColumn <Product, Integer> ProductInventoryColumn;
    @FXML
    private TableColumn <Product, String> ProductNameColumn;
    @FXML
    private TableColumn<Part, Integer> ProductIDColumn;
    @FXML
    private TableView <Product> ProductTableView;
    @FXML
    private TableView <Part> PartsTableView;
    @FXML
    private TableColumn <Part, Double> PartPriceColumn;
    @FXML
    private TableColumn <Part, Integer> PartInventoryColumn;
    @FXML
    private TableColumn <Part, String> PartNameColumn;
    @FXML
    private TableColumn<Part, Integer> PartIDColumn;
    @FXML
    private TextField PartSearchBar;
    @FXML
    private TextField ProductSearchBar;
    @FXML
    private Label PartsTableProcurator;
    @FXML
    private Label ProductTableProcurator;

    /**
     * Used to refresh the part table and provide filtered results
     */
    private void restorePartTable() {
        restorePartTable(false);
    }

    /**
     * Expansion of above function
     * @param reset
     */
    private void restorePartTable(boolean reset)
    {
        String SearchInput = PartSearchBar.getText();
        PartsTableView.setItems(Inventory.getFilteredPartList(SearchInput, reset));
        if (SearchInput.isEmpty())
        {
            PartsTableProcurator.setText("Click Add to Add A New Part.");
        }
        else
        {
            PartsTableProcurator.setText("No Items Could Be Found That Matched Your Search Query. Please Try Again.");
            if(PartsTableView.getItems().size()>0 )
            {
                PartsTableView.getSelectionModel().select(0);
            }
        }
    }
    private void restoreProductTable() {
        restoreProductTable(false);
    }


    /**
     * Reset function for refreshing the product table adn searching parts on main screen
     * @param reset
     */
    private void restoreProductTable(boolean reset) {
        String searchInput = ProductSearchBar.getText();
        ProductTableView.setItems(Inventory.getFilteredParts(searchInput);
        if (searchInput.isEmpty()) {
            ProductTableProcurator.setText("Click Add to add a new product.");
        } else {
            ProductTableProcurator.setText("No items matched your search query. Please try again.");
            if (ProductTableView.getItems().size() > 0) {
                ProductTableView.getSelectionModel().select(0);
            }
        }
    }

    /**
     * Handler Function for when a product search is performed
     * @param keyEvent
     */
    @FXML
    public void onProductSearch(KeyEvent keyEvent)
    {
        restoreProductTable();
    }
    @FXML
    public void OnPartSearch(KeyEvent keyEvent) {
        restorePartTable();
    }
    @FXML
    public void onModifyPartClick(ActionEvent actionEvent) throws IOException
    {
        //When the modify button is clicked for a part
        if(PartsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "A Part must be selected to perform a modification.");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        //Load the EditPartView
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader ViewLoader = LoadView(stage, "/views/EditPart.fxml");
        EditPart EditPartController = ViewLoader.getController();
        EditPartController.startPartEdit(PartsTableView.getSelectionModel().getSelectedIndex(), PartsTableView.getSelectionModel().getSelectedItem());

    }

    /**
     * Action event for adding parts and calling the Editpart view
     * @param actionEvent for clicking add part button
     * @throws IOException error to be thrown
     */
    @FXML
    public void onAddPartClick(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = LoadView(stage, "/view/EditPartView.fxml");
        EditPart editCtrl = loader.getController();
        editCtrl.addPart();
    }

    /**
     * Action event for deletion of a part
     * If successful will display a confirmation form
     * Otherwise, it will display an alert
     * @param actionEvent
     */
    @FXML
    public void onDeletePartClick(ActionEvent actionEvent) {
        if (PartsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a part to delete!");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        Part selectedPart = PartsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the following part?\n\n" + selectedPart.getName());
        AlertDisplayHelper(alert);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
            restorePartTable(true);
        }
    }

    /**
     * Action Event for adding a product
     * If succesful will call the edit product view
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void onActionAddProduct(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = loadView(stage, "/view/EditProductView.fxml");
        EditProductController editCtrl = loader.getController();
        editCtrl.startAdd();
    }


    /**
     * Action event for modifying a product. If no input is found it will display an alert
     * Otherwise, it calls the edit product view
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void onActionModifyProduct(ActionEvent actionEvent) throws IOException {
        if (ProductTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a product to modify!");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = loadView(stage, "/view/EditProductView.fxml");
        EditProduct editCtrl = loader.getController();
        editCtrl.startEdit(productTableView.getSelectionModel().getSelectedIndex(), productTableView.getSelectionModel().getSelectedItem());
    }


    /**
     * Action event for deleting a product
     * If no input is found it will display an alert, if there are parts associated it will display error message
     * Otherwise, it displays a confirmation alert
     * @param actionEvent
     */
    @FXML
    public void onDeleteProductClick(ActionEvent actionEvent) {
        if (ProductTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a product to delete!");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }
        if (!ProductTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The selected product has associated parts.\n" +
                    "Disassociate the parts before deleting.");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        Product selectedProduct = ProductTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the following product?\n\n" + selectedProduct.getName());
        AlertDisplayHelper(alert);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deleteProduct(selectedProduct);
            restoreProductTable(true);
        }
    }
    /**
     * Initialize
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
