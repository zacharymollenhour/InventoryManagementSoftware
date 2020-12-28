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


public class EditPart {
    //Initialize FXML Variables
    @FXML
    private Label EditPartLabel;

    @FXML
    private Label AltLabel;

    @FXML
    private RadioButton InHouseToggle;

    @FXML
    private RadioButton OutsourcedToggle;

    @FXML
    private TextField PartID;

    @FXML
    private TextField PartName;

    @FXML
    private TextField PartStock;

    @FXML
    private TextField PartPrice;

    @FXML
    private TextField PartMax;

    @FXML
    private TextField PartMin;

    @FXML
    private ToggleGroup PartSourceSelection;

    /**
     * Changes between machine ID and Company Name based on
     * if it is in-house or outsourced
     */
    @FXML
    private TextField AlternativeInputField;

    //Declare text strings
    private final String InHouseAltText = "Machine ID";
    private final String OutsourcedAltText = "Company Name";
    private final String NewPartIDPlaceHolder = "To be calculated";

    private Stage stage;

    //Start the PartIndex Value at -1
    private int PartIndex = -1;

    /**
     * Functions to handle the editing of a part
     */

    /**
     * Function that handles the view creation for a new part being added
     * Default value for the Source Toggle Group is In House until otherwise changed
     */
    public void addPart()
    {
        EditPartLabel.setText("Add Part");
        AltLabel.setText(InHouseAltText);

        //Default the Source to the InHouse
        InHouseToggle.setSelected(true);

        //Since the part is new
        //A part Id will be calculated
        PartID.setText(NewPartIDPlaceHolder);
    }

    /**
     * Function that handles when the cancel button is clicked
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void onPartCancelClick(ActionEvent actionEvent) throws IOException{
        //Display an alert for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All Changes will be discarded. Continue?");
        AlertDisplayHelper(alert);
        Optional<ButtonType> CancelResult = alert.showAndWait();
        if(CancelResult.isEmpty() || CancelResult.get() != ButtonType.OK)
        {
            return;
        }

        //If confirmed we will return to the main screen
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        LoadView(stage, "/view/Main.fxml");
    }


    /**
     * The In-House and Outsourced radio buttons switch the bottom label to the correct value (Machine ID or Company Name) and swap In-House parts and Outsourced parts. When new objects need to be created after the Save button is clicked, the part ID should be retained.
     * @param actionEvent
     */
    @FXML
    public void onPartSourceClick(ActionEvent actionEvent)
    {
        if(InHouseToggle.isSelected()) {
            //If In House, we will show the MachineID Text
            AltLabel.setText(InHouseAltText);
        }
        else if (OutsourcedToggle.isSelected())
        {
            //Otherwise, we show the text for Company Name
            AltLabel.setText(OutsourcedAltText);
        }

    }


    public void startPartEdit(int SelectedPartIndex, Part SelectedPart)
    {
        EditPartLabel.setText("Modify Part");
        //Check if it is a in house or outsourced
        if(SelectedPart instanceof Outsourced)
        {
            AltLabel.setText(OutsourcedAltText);
            OutsourcedToggle.setSelected(true);
            AlternativeInputField.setText(String.valueOf(((Outsourced) SelectedPart).getCompanyName()));
        }
        else if(SelectedPart instanceof InHouse)
        {
            AltLabel.setText(InHouseAltText);
            InHouseToggle.setSelected(true);
            AlternativeInputField.setText(String.valueOf(((InHouse) SelectedPart).getMachineId()));
        }

        //Fill in the remainder of the text fields
        PartStock.setText(String.valueOf(SelectedPart.getStock()));
        PartID.setText(String.valueOf(SelectedPart.getId()));
        PartMax.setText(String.valueOf(SelectedPart.getMax()));
        PartMin.setText(String.valueOf(SelectedPart.getMin()));
        PartName.setText(String.valueOf(SelectedPart.getName()));
        PartPrice.setText(String.valueOf(SelectedPart.getPrice()));
        PartIndex = SelectedPartIndex;
    }

    /**
     * Function that handles the Save Button being clicked for parts
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void onPartSaveClick(ActionEvent actionEvent) throws IOException {
        ArrayList<String> ValidatedDataErrors = new ArrayList<>();

        Part stagedPart;

        /**
         * Parse through the user inputted values
         * to prepare data to be saved
         */

        //Get the next available PartID using the Inventory Helper Function
        int partID = PartID.getText().equals(NewPartIDPlaceHolder) ? Inventory.getNextPartID() : Integer.parseInt(PartID.getText());

        //Use the DataValidation Class for validating inputted double and integer values
        int partStock = ValidateIntInput(PartStock, "Inv", ValidatedDataErrors);

        int max = ValidateIntInput(PartMax, "Max", ValidatedDataErrors);
        int min = ValidateIntInput(PartMin, "Min", ValidatedDataErrors);

        //Check that min is less than the max value
        if (min > max) {
            ValidatedDataErrors.add("Minimum Value must be less than the Maximum Value");
        }

        double partPrice = ValidateDoubleInput(PartPrice, ValidatedDataErrors, "Price/Cost Per Unit");
        String partName = PartName.getText();

        //Check that the inventory stock value is greater than the minimum and less than the max
        if (partStock < min || partStock > max) {
            ValidatedDataErrors.add("Inv must be greater than Min and less than Max");
        }

        //Inform end user of any errors that are in the ValidatedDataErrors list
        if (ValidatedDataErrors.size() > 0)
        {
            //If reached, there were errors
            Alert alert = AlertHelper(Alert.AlertType.ERROR, "One of more of the input fields failed to validate. Please review inputted data and make any necessary corrections.", String.join("\n", ValidatedDataErrors));

            //Helper for sizing
            AlertDisplayHelper(alert);

            alert.showAndWait();
            return;
        }

        //Initialize a Staged Part when necessary
        if(InHouseToggle.isSelected())
        {
            //Only perform machineID validation when source is set to inhouse
            int machineID = ValidateIntInput(AlternativeInputField, InHouseAltText, ValidatedDataErrors);
            //Create a staged part using the InHouse Class
            stagedPart = new InHouse(partID,partName,partPrice,partStock,min,max,machineID);
        }
        else{
            //Only perform CompanyName and Outsouced Class when Outsourced source is selected
            String companyName = AlternativeInputField.getText();
            stagedPart = new Outsourced(partID,partName,partPrice,partStock,min,max,companyName);


        }
        //Display confirmation window to user to confirm their actions
        String ConfirmationPrompt = String.format("ID: %d\nName: %s\nInventory: %d\nUnit Price: %.2f\nMax: %d\nMin: %d\n%s: %s",
                PartID, PartName, PartStock, PartPrice, max, min, AltLabel.getText(), AlternativeInputField.getText());


        Alert alert = AlertHelper(Alert.AlertType.CONFIRMATION, "Please review the details and confirm they are correct.", ConfirmationPrompt);
        AlertDisplayHelper(alert);
        Optional<ButtonType> ConfirmationResult = alert.showAndWait();
        if(ConfirmationResult.isEmpty() || ConfirmationResult.get() != ButtonType.OK)
        {
            return;
        }

        if(PartIndex >= 0)
        {
            //If partIndex is greater than 0 than we will update the existing part
            Inventory.updatePart(PartIndex, stagedPart);
        }
        else
        {
            //Otherwise, we will be adding a new part
            Inventory.addPart(stagedPart);
        }


        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        //Once complete, load the main view
        LoadView(stage, "/view/Main.fxml");
    }


}
