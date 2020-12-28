package Controller;
import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Part;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import static Core.DataValidation.ValidateDoubleInput;
import static Core.DataValidation.ValidateIntInput;
import static Core.Main.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * PART CONTROLLER CLASS
 * @author Zachary Mollenhour
 */
public class EditPart {

    //EDIT PART VIEW TITLE
    @FXML
    private Label EditPartLabel;

    //A VERSATILE LABEL USED FOR MACHINE ID AND COMPANY NAME
    @FXML
    private Label VersatileLabel;

    //RADIO BUTTON OPTION FOR IN-HOUSE
    @FXML
    private RadioButton InHouseToggle;

    //RADIO BUTTON OPTION FOR OUTSOURCED
    @FXML
    private RadioButton OutsourcedToggle;

    //TEXTFIELD INPUT FOR PARTID
    @FXML
    private TextField PartID;

    //TEXTFIELD INPUT FOR PARTNAME
    @FXML
    private TextField PartName;

    //TEXTFIELD INPUT FOR PARTSTOCK
    @FXML
    private TextField PartStock;

    //TEXT FIELD INPUT FOR PART PRICE
    @FXML
    private TextField PartPrice;

    //TEXTFIELD INPUT FOR PARTMAX
    @FXML
    private TextField PartMax;

    //TEXTFIELD INPUT FOR PARTMIN
    @FXML
    private TextField PartMin;

    //TOGGLE GROUP FOR THE SOURCE (IN-HOUSE, OUTSOURCED)
    @FXML
    private ToggleGroup PartSourceSelection;

    //TEXT FIELD USED WITH ABOVE VERSATILE LABEL
    //INTERCHANGEABLE WITH MACHINEID AND COMPANY NAME
    @FXML
    private TextField VersatileInputField;

    //DECLARE STRING VALUES FOR SOME OF THE IN TEXTFIELD STRINGS
    private final String InHouseVersatileText = "Machine ID";
    private final String OutsourcedVersatileText = "Company Name";
    private final String NewPartDefaultIDText = "To be calculated";

    //TITLE TEXT STRINGS
    private final String AddPart = "Add Part";
    private final String ModifyPart = "Modify Part";

    //CREATE STAGE VARIABLE FOR STAGING OF PARTS
    private Stage stage;

    //INITIALIZE PART INDEX AS -1
    private int PartIndex = -1;

    //*********************************//
    //ADD AND EDIT FUNCTIONS FOR A PART//
    //*********************************//

    /**
     * FUNCTION RESPONSIBLE FOR ADDING NEW PARTS
     */
    public void PartAdd()
    {
        //SET THE VIEW TITLE PANE TO ADD PART
        EditPartLabel.setText(AddPart);

        //ASSIGN DEFAULT VERSATILE LABEL (IN-HOUSE)
        VersatileLabel.setText(InHouseVersatileText);

        //RADIO TOGGLE DEFAULTS TO IN-HOUSE
        InHouseToggle.setSelected(true);

        //WHEN A NEW PART IS BEING CREATED
        //WE WILL USE A DEFAULT PARTID TEXT OF "TO BE CALCULATED"
        PartID.setText(NewPartDefaultIDText);
    }

    /**
     * FUNCTION RESPONSIBLE FOR EDITING OF EXISTING PARTS
     * @param SelectedPartIndex
     * @param EditingPart
     */
    public void PartEdit(int SelectedPartIndex, Part EditingPart)
    {
        //SET THE VIEW TITLE PANE TO MODIFY PART
        EditPartLabel.setText(ModifyPart);

        //CHECK WHETHER THE PART BEING EDITED
        //FALLS UNDER THE OUTSOURCED MODEL
        //OR THE IN HOUSE MODEL
        //UPDATE THE VERSATILE INPUT FIELD ACCORDINGLY
        if(EditingPart instanceof Outsourced)
        {
            //IF UNDER OUTSOURCED MODEL
            //VERSATILE TEXT IS COMPANY NAME
            VersatileLabel.setText(OutsourcedVersatileText);
            OutsourcedToggle.setSelected(true);
            VersatileInputField.setText(String.valueOf(((Outsourced) EditingPart).getCompanyName()));
        }
        else if(EditingPart instanceof InHouse)
        {
            //IF UNDER IN-HOUSE MODEL
            //VERSATILE TEXT IS MACHINE ID
            VersatileLabel.setText(InHouseVersatileText);
            InHouseToggle.setSelected(true);
            VersatileInputField.setText(String.valueOf(((InHouse) EditingPart).getMachineId()));
        }

        //ASSIGN VALUES FOR REMAINDER OF FIELDS
        PartStock.setText(String.valueOf(EditingPart.getStock()));
        PartID.setText(String.valueOf(EditingPart.getId()));
        PartMax.setText(String.valueOf(EditingPart.getMax()));
        PartMin.setText(String.valueOf(EditingPart.getMin()));
        PartName.setText(String.valueOf(EditingPart.getName()));
        PartPrice.setText(String.valueOf(EditingPart.getPrice()));
        PartIndex = SelectedPartIndex;
    }

    //****************************************//
    //END OF ADD AND EDIT FUNCTIONS FOR A PART//
    //****************************************//


    //*********************//
    //ACTION HANDLER EVENTS//
    //*********************//
    /**
     * FUNCTION RESPONSIBLE FOR PROCESSING THE ACTION EVENT
     * FOR CLICKING CANCEL IN THE PART VIEW
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void HandlePartCancel(ActionEvent actionEvent) throws IOException{
        //PROMPT THE USER WITH AN ALERT OF CONFIRMATION TYPE
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All Changes Will Be Discarded. Continue?");
        AlertDisplayHelper(alert);

        //CREATE BUTTON TO CONFIRM THE CHANGES TO BE DISCARDED
        Optional<ButtonType> CancelResult = alert.showAndWait();

        //CHECK IF THE USER HAS CONFIRMED, IF NOT RETURN
        if(CancelResult.isEmpty() || CancelResult.get() != ButtonType.OK)
        {
            return;
        }

        //IF THE USER HAS CONFIRMED THE ABOVE ALERT, WE CAN RETURN TO THE MAIN.FXML FILE
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        LoadView(stage, "/views/Main.fxml");
    }


    /**
     * FUNCTION RESPONSIBLE FOR HANDLING THE ACTION EVENT
     * FOR CHANGING SOURCES (IN-HOUSE OR OUTSOURCED)
     * @param actionEvent
     */
    @FXML
    public void HandlePartSourceChange(ActionEvent actionEvent)
    {
        //CHECK WHICH RADIO TOGGLE HAS BEEN SELECTED
        if(InHouseToggle.isSelected()) {
            //IF SOURCE IS IN-HOUSE, SHOW MACHINE ID
            VersatileLabel.setText(InHouseVersatileText);
        }
        else if (OutsourcedToggle.isSelected())
        {
            //OTHERWISE, SHOW COMPANY NAME
            VersatileLabel.setText(OutsourcedVersatileText);
        }
    }

    /**
     * FUNCTION RESPONSIBLE FOR HANDLING THE ACTION EVENT
     * FOR SAVING A PART
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void HandlePartSave(ActionEvent actionEvent) throws IOException {

        //CREATE AN ARRAY LIST TO STORE ANY ENCOUNTERED ERRORS
        ArrayList<String> Errors = new ArrayList<>();

        //CREATE A VARIABLE TO STORE THE STAGED PART
        Part stagedPart;

        //***************************************//
        //START PARSING INPUTTED DATA TO BE SAVED//
        //***************************************//

        //RETRIEVE THE NEXT AVAILABLE PART ID USING INVENTORY CLASS
        int partID = PartID.getText().equals(NewPartDefaultIDText) ? Inventory.getNextPartID() : Integer.parseInt(PartID.getText());

        //VALIDATE NUMBER INPUT USING THE DATA VALIDATION CLASS
        int partStock = ValidateIntInput(PartStock, "Inv", Errors);
        int partMax = ValidateIntInput(PartMax, "Max", Errors);
        int partMin = ValidateIntInput(PartMin, "Min", Errors);

        //****************************//
        //END OF INPUTTED DATA PARSING//
        //****************************//


        //****************//
        //DATA VALIDATIONS//
        //****************//

        //VALIDATE THAT THE PART MINIMUM VALUE IS LESS THAN THE PART MAXIMUM VALUE
        if (partMin > partMax) {
            Errors.add("Minimum Value must be less than the Maximum Value");
        }

        //VALIDATE THE COST IS A VALID DOUBLE
        double partPrice = ValidateDoubleInput(PartPrice, Errors, "Price/Cost Per Unit");

        //GET THE VALUE FOR THE PART NAME
        String partName = PartName.getText();

        //CHECK IF THE PART STOCK IS LESS THAN PART MINIMUM OR GREATER THAN PART MAXIMUM
        //IF SO, ADD ERROR LOG TO ERROR ARRAY
        if (partStock < partMin || partStock > partMax) {
            Errors.add("Inv must be greater than Min and less than Max");
        }

        //IF THE ERROR ARRAY CONTAINS ANY INSTANCES, DISPLAY AN ALERT TO THE USER OF ALERT TYPE ERROR
        if (Errors.size() > 0)
        {
            //IF WE MADE IT HERE, THERE WERE ERRORS
            Alert alert = AlertHelper(Alert.AlertType.ERROR, "One of more of the input fields failed to validate. Please review inputted data and make any necessary corrections.", String.join("\n", Errors));

            //ALERT SIZING HELPER
            AlertDisplayHelper(alert);

            //DISPLAY AND AWAIT INTERACTINO WITH THE ALERT
            alert.showAndWait();
            return;
        }

         //***********************//
        //END OF DATA VALIDATIONS//
        //***********************//


        //INITIALIZE A STAGED PART
        if(InHouseToggle.isSelected())
        {
            //GET THE VALUE AND VALIDATE THE INTEGER FOR THE MACHINE-ID USING DATA VALIDATION CLASS
            int machineID = ValidateIntInput(VersatileInputField, InHouseVersatileText, Errors);

            //IF IN-HOUSE, WE WILL INITIALIZE A STAGED PART USING THE IN-HOUSE MODEL AND MACHINE-ID
            stagedPart = new InHouse(partID,partName,partPrice,partStock,partMin,partMax,machineID);
        }
        else{
            //ONLY DONE FOR OUTSOURCED TOGGLE SELECTION

            //GET THE TEXT VALUE FOR THE INPUTTED COMPANY NAME
            String companyName = VersatileInputField.getText();

            //IF OUTSOURCED, WE WILL INITIALIZE A STAGED PART USING THE OUTSOURCED MODEL AND COMPANY NAME
            stagedPart = new Outsourced(partID,partName,partPrice,partStock,partMin,partMax,companyName);

        }

        //PROMPT USER WITH CONFIRMATION WINDOW TO CONFIRM THE PART DATA THEY WANT TO SAVE
        String SaveConfirmationPrompt = String.format("ID: %d\nName: %s\nInventory: %d\nUnit Price: %.2f\nMax: %d\nMin: %d\n%s: %s",
                PartID, PartName, PartStock, PartPrice, partMax, partMin, VersatileLabel.getText(), VersatileInputField.getText());

        //CREATE ALERT WINDOW OF CONFIRMATION TYPE
        Alert alert = AlertHelper(Alert.AlertType.CONFIRMATION, "Please review the details and confirm they are correct.", SaveConfirmationPrompt);
        AlertDisplayHelper(alert);

        //BUTTON FOR GETTING USER CONFIRMATION OF SAVING OF PART DATA
        Optional<ButtonType> ConfirmationResult = alert.showAndWait();

        //IF NO USER INTERACTION IS DONE RETURN
        if(ConfirmationResult.isEmpty() || ConfirmationResult.get() != ButtonType.OK)
        {
            return;
        }

        //CHECK IF THE PART BEING SAVED DOES NOT EXIST YET, IF SO ADD THE STAGEDPART
        if(PartIndex == 0)
        {
            Inventory.addPart(stagedPart);
        }
        else
        {
            //IF THE PART ALREADY EXISTS, WE WILL PERFORM AN UPDATE ON THE PART
            Inventory.updatePart(PartIndex, stagedPart);
        }

        //JAVAFX PROCESS TO LOAD BACK MAIN VIEW
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        //Once complete, load the main view
        LoadView(stage, "/views/Main.fxml");
    }

    //***********************//
    // END OF ACTION HANDLERS//
    //***********************//
}
