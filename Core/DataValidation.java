package Core;


import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * Helper class to handle input validation throughout all input boxes
 * @author Zachary Mollenhour
 */
public class DataValidation {

    /**
     * Helper Function to determine if a inputted value is a double
     * If it is, the value will be returned
     * Otherwise, a error will be added to the ValidationErrors List
     * @param input
     * @param ValidationErrors
     * @param FieldName
     * @return
     */
    public static double ValidateDoubleInput(TextField input, ArrayList<String> ValidationErrors, String FieldName)
    {
        //if statement to check the inputted textfield
        if (input.getText().matches("-?\\d+(\\.\\d+)?")) {
            //Only triggers if the inputted text was in fact a double
            return Double.parseDouble(input.getText());
        }
        ValidationErrors.add(FieldName + " Value must be a Decimal Number");
        return 0;
    }

    public static int ValidateIntInput(TextField input, String fieldName, ArrayList<String> ValidationErrors)
    {
        //if statement to check the inputted textfield
        if (input.getText().matches("\\d+")) {
            //Only triggers if the inputted text was in fact a Integer
            return Integer.parseInt(input.getText());
        }
        ValidationErrors.add(fieldName + " Value must be a positive integer.");
        return 0;
    }
}
