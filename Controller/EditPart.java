package Controller;

import Models.Inventory;
import Models.Part;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import Core.Main;
import static Core.DataValidation.ValidateDoubleInput;
import static Core.DataValidation.ValidateIntInput;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

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

    @FXML
    public void onPartSaveClick(ActionEvent actionEvent) throws IOException{
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

        int max = ValidateIntInput(PartMax,"Max",ValidatedDataErrors);
        int min = ValidateIntInput(PartMin,"Min",ValidatedDataErrors);

        //Check that min is less than the max value
        if(min > max)
        {
            ValidatedDataErrors.add("Minimum Value must be less than the Maximum Value");
        }

        double partPrice = ValidateDoubleInput(PartPrice,ValidatedDataErrors,"Price/Cost Per Unit");
        String partName = PartName.getText();

        //Check that the inventory stock value is greater than the minimum and less than the max
        if(partStock < min || partStock > max)
        {
            ValidatedDataErrors.add("Inv must be greater than Min and less than Max");
        }


    }

}
