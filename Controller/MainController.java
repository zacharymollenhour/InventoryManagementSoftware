package Controller;
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

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
