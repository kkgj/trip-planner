import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.image.Image ;
import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import java.util.*;

public class Main extends Application {
    ListView<City> listView;
    ListView<City> possibleStop;
    ObservableList<City> observableList;
    private ImageView pic;
    private Image image;
    private GraphicsContext gc;
    String title = "Trip Planner";
    String city;
    String state;
    double LaD;
    double LaM;
    double LoD;
    double LoM;
    int index;
    private TextField State;
    private TextField Citi;
    private TextField LatitudeDegrees;
    private TextField LatitudeMinutes;
    private TextField LongitudeDegrees;
    private TextField LongitudeMinutes;
    File file;
    ArrayList<City> AL = new ArrayList<>();
    ArrayList<City> trip = new ArrayList<>();
    Label la = new Label("Total Mileage: ");
    Pane pane;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(title);
        image = new Image("usa_map.png");

        HBox top = new HBox();
        Button button1 = new Button("New");
        Button button2 = new Button("Save");
        Button button3 = new Button("Load");
        top.getChildren().addAll(button1,button2,button3);
        top.setSpacing(30);
        top.setPadding(new Insets(0,20,0,40));

        button2.setOnAction(event -> {
            try{
                PrintWriter printWriter = new PrintWriter ("src/PossibleStop.txt");
                for(City i:AL){
                    printWriter.println (i.getCityName()+"@"+i.getState()+"@"+i.getLatDeg()+
                            "@"+i.getLatMin()+"@"+ i.getLongDeg()+"@"+ i.getLongMin());
                }
                printWriter.flush();
                printWriter.close ();
            }catch (Exception e){
                System.out.println("You got error.");
            }
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                saveTextToFile(trip, file);
            }
        });

        button1.setOnAction(event ->{
            possibleStop.getItems().clear();
            AL.clear();
            setPossibleStopArrayList();
            setPossibleStop();
            AlertBox.display();
            title = AlertBox.a;
            primaryStage.setTitle(title);
            trip.clear();
            listView.getItems().clear();
            pane.getChildren().clear();
            pane.getChildren().add(pic);
        });

        button3.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            loadTrip(file);
            setTrip();
            drawTrip();

        });


        listView = new ListView<>();
        listView.setMaxSize(2000,340);

        Label label = new Label("Trip Stops");
        HBox hBox = new HBox();
        Button button4 = new Button("+");
        Button button5 = new Button("-");
        hBox.getChildren().addAll(label,button4,button5);
        hBox.setSpacing(30);
        hBox.setPadding(new Insets(0,70,0,10));

        VBox right = new VBox();
        right.getChildren().addAll(hBox,listView,la);
        right.setSpacing(10);
        right.setPadding(new Insets(0,22,0,0));

        VBox vBox = new VBox();
        possibleStop = new ListView<>();
        Label label1 = new Label("Possible Stops");
        vBox.getChildren().addAll(label1,possibleStop);

        setPossibleStopArrayList();
        setPossibleStop();

        button4.setOnAction(event -> {
            City temp = possibleStop.getSelectionModel().getSelectedItem();
            listView.getItems().add(temp);
            trip.add(temp);
            la.setText("Total Mileage: "+Math.round(totalDistance()));
            pane.getChildren().clear();
            pane.getChildren().add(pic);
            drawTrip();
        });
        button5.setOnAction(event -> {
            City temp = listView.getSelectionModel().getSelectedItem();
            listView.getItems().remove(temp);
            trip.remove(temp);
            la.setText("Total Mileage: "+Math.round(totalDistance()));
            pane.getChildren().clear();
            pane.getChildren().add(pic);
            drawTrip();
        });

        Citi = new TextField();
        State = new TextField();
        LatitudeDegrees = new TextField();
        LatitudeMinutes = new TextField();
        LongitudeDegrees = new TextField();
        LongitudeMinutes = new TextField();
        Button button6 = new Button("Update");
        LatitudeMinutes.setOnAction(event -> {
            checkDouble(isDouble(LatitudeMinutes),LatitudeMinutes,LaM);
            if(LatitudeMinutes.getText()==null || LatitudeMinutes.getText().trim().isEmpty()){
                LatitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(!checkInput(LatitudeMinutes)) {
                    LatitudeMinutes.setStyle("");
                    button6.setDisable(false);
                }else{
                    LatitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });
        LatitudeDegrees.setOnAction(event -> {
            checkDouble(isDouble(LatitudeDegrees),LatitudeDegrees,LaD);
            if(LatitudeDegrees.getText()==null || LatitudeDegrees.getText().trim().isEmpty()){
                LatitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(!checkInput(LatitudeDegrees)) {
                    LatitudeDegrees.setStyle("");
                    button6.setDisable(false);
                }else{
                    LatitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });
        LongitudeDegrees.setOnAction(event -> {
            checkDouble(isDouble(LongitudeDegrees),LongitudeDegrees,LoD);
            if(LongitudeDegrees.getText()==null || LongitudeDegrees.getText().trim().isEmpty()){
                LongitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(!checkInput(LongitudeDegrees)) {
                    LongitudeDegrees.setStyle("");
                    button6.setDisable(false);
                }else{
                    LongitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });
        LongitudeMinutes.setOnAction(event -> {
            checkDouble(isDouble(LongitudeMinutes),LongitudeMinutes,LoM);
            if(LongitudeMinutes.getText()==null || LongitudeMinutes.getText().trim().isEmpty()){
                LongitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(!checkInput(LongitudeMinutes)) {
                    LongitudeMinutes.setStyle("");
                    LoM = Double.parseDouble(LongitudeMinutes.getText());
                    button6.setDisable(false);
                }else{
                    LongitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });

        LongitudeMinutes.setOnAction(event -> {
            checkDouble(isDouble(LongitudeMinutes),LongitudeMinutes,LoM);
            if(LongitudeMinutes.getText()==null || LongitudeMinutes.getText().trim().isEmpty()){
                LongitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(!checkInput(LongitudeMinutes)) {
                    LongitudeMinutes.setStyle("");
                    button6.setDisable(false);
                }else{
                    LongitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });

        Citi.setOnAction(event -> {
            if(Citi.getText()==null || Citi.getText().trim().isEmpty()){
                Citi.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(checkInput(Citi)) {
                    Citi.setStyle("");
                    button6.setDisable(false);
                }else{
                    Citi.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });

        State.setOnAction(event -> {
            if(State.getText()==null || State.getText().trim().isEmpty()){
                State.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                button6.setDisable(true);
            } else {
                if(checkInput(State)) {
                    State.setStyle("");
                    button6.setDisable(false);
                }else{
                    State.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    button6.setDisable(true);
                }
            }
        });

        button6.setOnAction(event -> {
            if((LatitudeMinutes.getText()==null && LatitudeMinutes.getText().trim().isEmpty())&&(State.getText()==null || State.getText().trim().isEmpty()) &&(Citi.getText()==null || Citi.getText().trim().isEmpty())&& (LongitudeMinutes.getText()==null ||
                    LongitudeMinutes.getText().trim().isEmpty())&&(LongitudeDegrees.getText()==null || LongitudeDegrees.getText().trim().isEmpty())&&(LatitudeDegrees.getText()==null || LatitudeDegrees.getText().trim().isEmpty())){
                button6.setDisable(true);
                State.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                Citi.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                LatitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                LongitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                LongitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                LatitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
            } else {
                state = State.getText();
                city = Citi.getText();
                //if(isDouble(LongitudeMinutes)&&isDouble(LongitudeDegrees))
                LoM = Double.parseDouble(LongitudeMinutes.getText());
                //if(isDouble(LongitudeDegrees))
                LoD = Double.parseDouble(LongitudeDegrees.getText());
                //if(isDouble(LatitudeDegrees))
                LaD = Double.parseDouble(LatitudeDegrees.getText());
                //if(isDouble(LatitudeMinutes))
                LaM = Double.parseDouble(LatitudeMinutes.getText());
                AL.add(new City(city,state,LaD,LaM,LoD,LoM));
//                AL.remove(index);
                try{
                    PossibleStopOrder();
                }catch (Exception e){
                    e.printStackTrace();
                }
                possibleStop.getItems().clear();
                setPossibleStop();
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(new Label("City:"), 0, 0);
        gridPane.add(Citi, 1, 0);
        gridPane.add(new Label("State:"), 0, 1);
        gridPane.add(State, 1, 1);
        gridPane.add(new Label("Latitude Degrees:"), 0, 2);
        gridPane.add(LatitudeDegrees, 1, 2);
        gridPane.add(new Label("Latitude Minutes:"), 0, 3);
        gridPane.add(LatitudeMinutes, 1, 3);
        gridPane.add(new Label("Longitude Degrees:"), 0, 4);
        gridPane.add(LongitudeDegrees, 1, 4);
        gridPane.add(new Label("Longitude Minutes:"), 0, 5);
        gridPane.add(LongitudeMinutes,1,5);
        gridPane.add(button6, 1, 6);

        gridPane.add(new Label("(press ENTER to check value)"), 2, 0);
        gridPane.add(new Label("(press ENTER to check value)"), 2, 1);
        gridPane.add(new Label("(press ENTER to check value)"), 2, 2);
        gridPane.add(new Label("(press ENTER to check value)"), 2, 3);
        gridPane.add(new Label("(press ENTER to check value)"), 2, 4);
        gridPane.add(new Label("(press ENTER to check value)"), 2, 5);


        HBox temp = new HBox();
        Button plus = new Button("+");
        plus.setOnAction(event -> {
            button6.setDisable(false);
            Clear();
        });
        Button minus = new Button("-");
        temp.getChildren().addAll(plus,minus);
        temp.setSpacing(10);
        temp.setPadding(new Insets(0,0,10,0));
        VBox BottomRight = new VBox();
        BottomRight.getChildren().addAll(temp,gridPane);
        BottomRight.setPadding(new Insets(20,20,0,30));

        minus.setOnAction(event -> {
            City Temp = possibleStop.getSelectionModel().getSelectedItem();
            AL.remove(Temp);
            possibleStop.getItems().clear();
            setPossibleStop();
        });



        HBox bottom = new HBox();
        bottom.getChildren().addAll(vBox,BottomRight);
        bottom.setPadding(new Insets(0,0,0,15));


//        Line line = new Line();
//        line.setStartX(0.0f);
//        line.setStartY(0.0f);
//        line.setEndX(100.0f);
//        line.setEndY(100.0f);
//        line.setStroke(Color.RED);
//        gridPane.add(line,0,1);

        pic = new ImageView();
        pic.setFitWidth(580);
        pic.setFitHeight(310);
        pic.setImage(image);
        //borderPane.setLeft(pic);

//        double pixelPerLatitude = 310.0/25;
//        double pixelPerLongitude = 580.0/60;
//        double y = 310-(40.7-25)*pixelPerLatitude;
//        double x = 580-(74-65)*pixelPerLongitude;

//        Circle circle = new Circle();
//        circle.setCenterX(x);
//        circle.setCenterY(y);
//        circle.setRadius(2.5);
//        circle.setFill(Color.RED);

        pane = new Pane();
        pane.setPadding(new Insets(0,0,0,0));
        pane.getChildren().add(pic);
       // pane.getChildren().add(circle);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(top);
        borderPane.setRight(right);
        borderPane.setLeft(pane);
        borderPane.setBottom(bottom);
        borderPane.setPadding(new Insets(0,0,0,20));



        Scene scene = new Scene(borderPane,900,1000);
        primaryStage.setScene(scene);

        primaryStage.show();
        try {
            possibleStop.setOnMouseClicked(e -> {
                City Temp = possibleStop.getSelectionModel().getSelectedItem();
                Citi.setStyle("");
                State.setStyle("");
                LatitudeDegrees.setStyle("");
                LatitudeMinutes.setStyle("");
                LongitudeMinutes.setStyle("");
                LongitudeDegrees.setStyle("");
                try{
                    LongitudeMinutes.setText(Double.toString(Temp.getLongMin()));
                    LatitudeDegrees.setText(Double.toString(Temp.getLatDeg()));
                    LongitudeDegrees.setText(Double.toString(Temp.getLongDeg()));
                    State.setText(Temp.getState());
                    Citi.setText(Temp.getCityName());
                    LatitudeMinutes.setText(Double.toString(Temp.getLatMin()));
                } catch (Exception ep){
                    System.out.println("You can't do that");
                }


                button6.setOnAction(event -> {
                    if((LatitudeMinutes.getText()==null || LatitudeMinutes.getText().trim().isEmpty())&&(State.getText()==null || State.getText().trim().isEmpty()) &&(Citi.getText()==null || Citi.getText().trim().isEmpty())&& (LongitudeMinutes.getText()==null ||
                            LongitudeMinutes.getText().trim().isEmpty())&&(LongitudeDegrees.getText()==null || LongitudeDegrees.getText().trim().isEmpty())&&(LatitudeDegrees.getText()==null || LatitudeDegrees.getText().trim().isEmpty())){
                        button6.setDisable(true);
                        State.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                        Citi.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                        LatitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                        LongitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                        LongitudeMinutes.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                        LatitudeDegrees.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
                    } else{
                        state = State.getText();
                        city = Citi.getText();
                        LoM = Double.parseDouble(LongitudeMinutes.getText());
                        LoD = Double.parseDouble(LongitudeDegrees.getText());
                        LaD = Double.parseDouble(LatitudeDegrees.getText());
                        LaM = Double.parseDouble(LatitudeMinutes.getText());
                        AL.add(new City(city,state,LaD,LaM,LoD,LoM));
                        AL.remove(Temp);
                        try{
                            PossibleStopOrder();
                        }catch (Exception e2){
                            e2.printStackTrace();
                        }
                        possibleStop.getItems().clear();
                        Clear();
                        setPossibleStop();
//                        Clear();
                        }
                });
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkInput(TextField input){
        if(input.getText().matches("[a-z A-Z]*")) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkInputNum(TextField input){
        if(input.getText().matches("[0-9]*")) {
            return true;
        }
        else {
            return false;
        }
    }

    private void Clear(){
        State.clear();
        State.setStyle("");
        Citi.clear();
        Citi.setStyle("");
        LatitudeDegrees.clear();
        LatitudeDegrees.setStyle("");
        LatitudeMinutes.clear();
        LatitudeMinutes.setStyle("");
        LongitudeDegrees.clear();
        LongitudeDegrees.setStyle("");
        LongitudeMinutes.clear();
        LongitudeMinutes.setStyle("");

    }

    public boolean isDouble(TextField input){
        try{
            double number = Double.parseDouble(input.getText());
            input.setStyle("");
            return true;
        }catch (Exception e){
            input.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");
            return false;
        }
    }


    public void checkDouble(boolean a, TextField input, double b){
        if(a){
            b = Double.parseDouble(input.getText());
            input.setStyle("");
        } else {
            input.setStyle("-fx-border-color:#FF0000; -fx-border-width:2px; -fx-border-radius:5px;");

        }
    }

    public void setPossibleStop(){
        for(City c:AL){
            possibleStop.getItems().add(c);
        }
    }

    public void setPossibleStopArrayList(){
        Scanner x;
        file = new File("src/PossibleStop.txt");
        try {
            x = new Scanner(file);
            String str;
            while(x.hasNextLine()){
                str = x.nextLine();
                String[] a = str.split("@");
                try {
                    City city = new City(a[0],a[1],Double.parseDouble(a[2]),Double.parseDouble(a[3]),Double.parseDouble(a[4]),Double.parseDouble(a[5]));
                    AL.add(city);
                    PossibleStopOrder();
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            x.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void PossibleStopOrder() throws Exception{
        for(int i = 0; i<AL.size()-1;i++){
            int min_idx = i;
            for(int j = i+1 ; j < AL.size() ; j++){
                if(AL.get(j).getCityName().compareTo(AL.get(min_idx).getCityName())<0){
                    min_idx = j;
                }
                Collections.swap(AL,i,min_idx);
            }
        }
    }

    public double totalDistance() {
        double total = 0;
        for(int i = 0; i<trip.size()-1;i++){
            double x = (Math.sin((trip.get(i).getLatDeg()+trip.get(i).getLatMin()/60.0)/(180/Math.PI))*Math.sin((trip.get(i+1).getLatDeg()+trip.get(i+1).getLatMin()/60.0)/(180/Math.PI)))+(Math.cos((trip.get(i).getLatDeg()+trip.get(i).getLatMin()/60.0)/(180/Math.PI))*Math.cos((trip.get(i+1).getLatDeg()+
                    trip.get(i+1).getLatMin()/60.0)/(180/Math.PI))*Math.cos(((trip.get(i+1).getLongDeg()+trip.get(i+1).getLongMin()/60.0)/(180/Math.PI))- ((trip.get(i).getLongDeg()+trip.get(i).getLongMin()/60.0)/(180/Math.PI))));
            double d = 6371 * Math.atan((Math.sqrt(1 - Math.pow(x, 2))/x));
            total += d;
        }
        return total;
    }

    public void drawTrip(){
        double pixelPerLatitude = 310.0/25;
        double pixelPerLongitude = 580.0/60;
        if(trip.size()>1){
            double yTemp = 310-((trip.get(0).latDeg+trip.get(0).getLatMin()/60.0)-25)*pixelPerLatitude;
            double xTemp = 580-((trip.get(0).longDeg+trip.get(0).getLongMin()/60.0)-65)*pixelPerLongitude;
            Circle cTemp = new Circle(xTemp,yTemp,2.5);
            cTemp.setFill(Color.RED);
            pane.getChildren().add(cTemp);
            for(int i = 1; i<trip.size(); i++){
                double y = 310-((trip.get(i).latDeg+trip.get(i).getLatMin()/60.0)-25)*pixelPerLatitude;
                double x = 580-((trip.get(i).longDeg+trip.get(i).getLongMin()/60.0)-65)*pixelPerLongitude;
                Circle circle = new Circle(x,y,2.5);
                circle.setFill(Color.RED);
                Line line = new Line();
                double xLine = 580-((trip.get(i-1).longDeg+trip.get(i-1).getLongMin()/60.0)-65)*pixelPerLongitude;
                double yLine = 310-((trip.get(i-1).latDeg+trip.get(i-1).getLatMin()/60.0)-25)*pixelPerLatitude;
                line.setStartX(xLine);
                line.setStartY(yLine);
                line.setEndX(x);
                line.setEndY(y);
                line.setStroke(Color.WHITE);
                pane.getChildren().add(line);
                pane.getChildren().add(circle);
            }
        }else if(trip.size()==1){
            double y = 310-((trip.get(0).latDeg+trip.get(0).getLatMin()/60.0)-25)*pixelPerLatitude;
            double x = 580-((trip.get(0).longDeg+trip.get(0).getLongMin()/60.0)-65)*pixelPerLongitude;
            Circle circle = new Circle(x,y,2.5);
            circle.setFill(Color.RED);
            pane.getChildren().add(circle);
        }
    }

    private void saveTextToFile(ArrayList<City> trip, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            for(City i: trip){
                writer.println(i.getCityName()+"@"+i.getState()+"@"+i.getLatDeg()+"@"+i.getLatMin()+"@"+i.getLongDeg()+"@"+i.getLongMin());
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {ex.printStackTrace();
        }
    }

    public void setTrip(){
        for(City c:trip){
            listView.getItems().add(c);
        }
    }

    public void loadTrip(File file){
        Scanner x;
        try {
            x = new Scanner(file);
            String str;
            while(x.hasNextLine()){
                str = x.nextLine();
                String[] a = str.split("@");
                try {
                    City city = new City(a[0],a[1],Double.parseDouble(a[2]),Double.parseDouble(a[3]),Double.parseDouble(a[4]),Double.parseDouble(a[5]));
                    trip.add(city);
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            x.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

