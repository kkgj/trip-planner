import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    static String a;

    public static String display(){
        Stage window  = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New Trip");
        Label label = new Label("Enter trip name");
        TextField textField = new TextField();
        Button button = new Button("ok");
        window.setMinWidth(350);
        window.setMinHeight(300);
        button.setOnAction(event -> {
            window.close();
        });

        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label,textField,button);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
        return a = textField.getText();
    }
}
