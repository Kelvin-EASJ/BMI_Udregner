import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Panel p to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Weight: "));

        BorderPane paneForTextField2 = new BorderPane();
        paneForTextField2.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField2.setStyle("-fx-border-color: green");
        paneForTextField2.setLeft(new Label("Height: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        TextField tf2 = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField2.setCenter(tf2);

        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);
        mainPane.setCenter(paneForTextField2);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        tf.setOnAction(e -> {
            try {


                // Get the weight from the text field
                double weight;
                weight = Double.parseDouble(tf2.getText().trim());

                //Get the height from the text field
                double height;
                height = Double.parseDouble(tf.getText().trim());

                // Send the weight and height to the server
                toServer.writeDouble(height);
                toServer.writeDouble(weight);
                toServer.flush();


                // Get BMI from the server
                double BMI = fromServer.readDouble();

                // Display to the text area
                ta.appendText("weight is " + weight + " height is " + height + "\n");
                ta.appendText("BMI received from the server is "
                        + BMI + '\n');
            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        });

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("130.254.204.36", 8000);
            // Socket socket = new Socket("drake.Armstrong.edu", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}