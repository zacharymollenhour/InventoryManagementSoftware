package Core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    /**
     * Main Function that launches args
     * @param args
     */
    public static void main(String [] args)
    {

        launch(args);
    }

    /**
     * Function that handles the Loading of views
     * @param stage
     * @param View
     * @return
     * @throws IOException
     */
    public static FXMLLoader LoadView(Stage stage, String View) throws IOException{

        //Initialize a FXMLLoader
        FXMLLoader LoaderHolder = new FXMLLoader();

        //Set the location for the view being loaded to the view param passed in
        LoaderHolder.setLocation(Main.class.getResource(View));

        //Load the view
        LoaderHolder.load();

        Scene scene = new Scene(LoaderHolder.getRoot());

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        //return the LoaderHolder
        return LoaderHolder;
    }


    /**
     * Used for showing alerts in the JavaFX Application
     * @param alertType
     * @param AlertDetails
     * @param AlertMessage
     * @return
     */
    public static Alert AlertHelper(Alert.AlertType alertType,String AlertDetails, String AlertMessage)
    {
        //Create an alert of given Type
        Alert alert = new Alert(alertType);

        Label TopMessageLabel = new Label(AlertMessage);
        TextArea AlertDetailsText = new TextArea(AlertDetails);

        //Set details for the alert box sizing
        AlertDetailsText.setEditable(false);
        AlertDetailsText.setMaxHeight(Double.MAX_VALUE);
        AlertDetailsText.setWrapText(true);
        GridPane.setHgrow(AlertDetailsText, Priority.ALWAYS);
        GridPane Body = new GridPane();
        Body.add(AlertDetailsText, 0, 1);
        Body.add(TopMessageLabel,0,0);
        alert.getDialogPane().setContent(Body);
        return alert;
    }

    /**
     * Wrapper that helps to adjust the sizing of an alert box
     * @param alert
     */
    public static void AlertDisplayHelper(Alert alert)
    {
        alert.setResizable(true);
        Platform.runLater(() -> alert.setResizable(false));

    }

    /**
     * Programs Main Entry Point
     * The Start function is called after the initialize and will be used to
     * load the view for Main.fxml
     * This is our default entry scene
     * @param stage
     */
    @Override
    public void start(Stage stage) throws Exception {
        LoadView(stage, "/views/Main.fxml");
        stage.setTitle("Inventory Management System");

    }
}
