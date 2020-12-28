package Controller;
import Models.Inventory;
import Models.Product;
import javafx.fxml.FXML;
import Models.Part;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
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
public class MainView implements Initializable {
    //*********************************//
    //Initialize JavaFX Instance Values//
    //*********************************//

    Stage StageHolder;

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

    //****************************************//
    //END OF INITIALIZE JAVAFX INSTANCE VARIABLES//
    //****************************************//

    //**************************//
    //START OF PRODUCT FUNCTIONS//
    //**************************//

    /**
     * Caller Function for below definition
     */
    private void RestoreProductTableView() {
        RestoreProductTableView(false);
    }

    /**
     * Reset function for refreshing the product table adn searching parts on main screen
     * @param IsReset
     */
    private void RestoreProductTableView(boolean IsReset) {
        //Start by getting text value for Product Query
        String ProductQuery = ProductSearchBar.getText();

        //Pass Product Query to Filtering Function
        ProductTableView.setItems(Inventory.getFilteredProducts(ProductQuery,IsReset));

        //Check if any products are returned from the query
        if (ProductQuery.isEmpty()) {
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
    public void HandleProductQuery(KeyEvent keyEvent)
    {
        RestoreProductTableView();
    }

    /**
     * Action Event for adding a product
     * If succesful will call the edit product view
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void HandleAddProduct(ActionEvent actionEvent) throws IOException {
        StageHolder = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = LoadView(StageHolder, "/views/EditProduct.fxml");
        EditProduct editCtrl = loader.getController();
        editCtrl.startAdd();
    }


    /**
     * Action event for modifying a product. If no input is found it will display an alert
     * Otherwise, it calls the edit product view
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void HandleProductModify(ActionEvent actionEvent) throws IOException {
        if (ProductTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a product to modify!");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        StageHolder = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = LoadView(StageHolder, "/view/EditProductView.fxml");
        EditProduct editCtrl = loader.getController();
        editCtrl.productEdit(ProductTableView.getSelectionModel().getSelectedIndex(), ProductTableView.getSelectionModel().getSelectedItem());
    }


    /**
     * Action event for deleting a product
     * If no input is found it will display an alert, if there are parts associated it will display error message
     * Otherwise, it displays a confirmation alert
     * @param actionEvent
     */
    @FXML
    public void HandleProductDelete(ActionEvent actionEvent) {
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
            RestoreProductTableView(true);
        }
    }
    //***************************//
    //END OF PRODUCT FUNCTIONS//
    //***************************//

    //***********************//
    //START OF PART FUNCTIONS//
    //***********************//

    /**
     * Function that refreshes the values in the part table
     */
    private void RestorePartTableView() {
        RestorePartTableView(false);
    }

    /**
     * Expansion of above function
     * @param IsReset
     */
    private void RestorePartTableView(boolean IsReset)
    {
        //Start by getting the queried part
        String PartQuery = PartSearchBar.getText();

        //Send that queried part to the filtering list function
        PartsTableView.setItems(Inventory.getFilteredPartList(PartQuery, IsReset));
        if (PartQuery.isEmpty())
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
    /**
     * Action event for deletion of a part
     * If successful will display a confirmation form
     * Otherwise, it will display an alert
     * @param actionEvent
     */
    @FXML
    public void HandlePartDelete(ActionEvent actionEvent) {
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
            RestorePartTableView(true);
        }
    }

    /**
     * Action event for adding parts and calling the Editpart view
     * @param actionEvent for clicking add part button
     * @throws IOException error to be thrown
     */
    @FXML
    public void HandlePartAdd(ActionEvent actionEvent) throws IOException {
        StageHolder = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = LoadView(StageHolder, "/views/EditPart.fxml");
        EditPart editCtrl = loader.getController();
        editCtrl.PartAdd();
    }

    @FXML
    public void HandlePartSearch(KeyEvent keyEvent) {
        RestorePartTableView();
    }
    @FXML
    public void HandlePartModify(ActionEvent actionEvent) throws IOException
    {
        //When the modify button is clicked for a part
        if(PartsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "A Part must be selected to perform a modification.");
            AlertDisplayHelper(alert);
            alert.showAndWait();
            return;
        }

        //Load the EditPartView
        StageHolder = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader ViewLoader = LoadView(StageHolder, "/views/EditPart.fxml");
        EditPart EditPartController = ViewLoader.getController();
        EditPartController.PartEdit(PartsTableView.getSelectionModel().getSelectedIndex(), PartsTableView.getSelectionModel().getSelectedItem());

    }
    //*********************//
    //END OF PART FUNCTIONS//
    //*********************//

    //*************************//
    //USER NAVIGATION FUNCTIONS//
    //*************************//

    @FXML
    public void HandleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    //********************************//
    //END OF USER NAVIGATION FUNCTIONS//
    //********************************//

    /**
     * Initialize
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //**********************//
        //SET PART COLUMN VALUES//
        //**********************//

        PartsTableView.setItems(Inventory.getAllParts());
        PartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //*************************//
        //END OF PART COLUMN VALUES//
        //*************************//

        //*************************//
        //SET PRODUCT COLUMN VALUES//
        //*************************//

        ProductTableView.setItems(Inventory.getAllProducts());
        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //****************************//
        //END OF PRODUCT COLUMN VALUES//
        //****************************//
    }
}
