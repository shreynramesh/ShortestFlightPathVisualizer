// --== CS400 File Header Information ==--
// Name: Vishesh Dhawan
// Email: vdhawan2@wisc.edu
// Team: AL Blue
// TA: Yelun Bao
// Lecturer: Dahl
// Notes to Grader: I have used the ArcGIS API and their documentation in this project for the map as well as map based elements.

package com.mycompany.app;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * App class extends the application class and is used to model the Flight Planner App
 */
public class App extends Application implements IApp {

    // create a new graphics overlay
    private GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
    //declare a new mapView
    private MapView mapView;

    //declare a new popup for searching for trips
    private Popup newSearchButtonPopUp;

    //declare main stage
    private Stage mainStage;

    //create a new airportGraph
    AirportGraph airportGraph = new AirportGraph();

    // ArrayList to hold all the airports after the data is loaded
    ArrayList<IAirport> allAirports = new ArrayList<>();

    // ArrayList to hold all the airports toTrieString() after the data is loaded
    ArrayList<String> allAirportsTrieString = new ArrayList<>();

    //hashtable for airports
    Hashtable<String, IAirport> airportHashtable = new Hashtable<>();

    //create airportLoader
    AirportLoader airportLoader = new AirportLoader();

    /**
     * start method for starting the program
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    @Override
    public void start(Stage stage) {

        //load the allFlights arraylist
        try {
            allAirports = airportLoader.loadFlights("./src/main/java/com/mycompany/app/flightData.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //load the airport hashtable
        airportHashtable = airportGraph.loadAirportHashtable(allAirports);

        //set the mainStage to stage
        mainStage = stage;

        // set the title and size of the stage and show it
        mainStage.setTitle("Flight Planner");
        mainStage.setWidth(800);
        mainStage.setHeight(700);
        mainStage.show();
        mainStage.setMaximized(true);

        // create a JavaFX scene with a stack pane as the root node, and add it to the scene
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        mainStage.setScene(scene);

        // set api key
        String yourApiKey = "AAPKb24538c98f4842b48aaaae0a46a9376fpovczmJ8lZoO60N5Ghr6us0QcQne4Ziv_48fk8SevTHzkPYzAGqKdV7NZg179v7j";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);

        // create a map view to display the map and add it to the stack pane
        mapView = new MapView();
        stackPane.getChildren().add(mapView);

        //select map type
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);

        // set the map on the map view
        mapView.setMap(map);
        mapView.setViewpoint(new Viewpoint(34.02700, -118.80543, 144447.638572));

        //add a graphics overlay
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        //call retrieveDataFromUser() method to show the "popup" style screen for user input
        retrieveDataFromUser();
    }

    /**
     * method adds points on the map for each airport
     *
     * @param airports, list of airports
     */
    private void addPoints(List<IAirport> airports) {

        // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
        SimpleMarkerSymbol simpleMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF5733, 10);
        SimpleLineSymbol blueOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF0063FF, 2);
        simpleMarkerSymbol.setOutline(blueOutlineSymbol);

        //iterate through airports arraylist
        for (int i = 0; i < airports.size(); i++) {
            // create a point geometry with a location and spatial reference
            String latLong = airports.get(i).getLatitude() + "* | " + airports.get(i).getLongitude() + "*";
            Point point = CoordinateFormatter.fromLatitudeLongitude(latLong, SpatialReferences.getWgs84());

            // create a graphic with the point geometry and symbol
            Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);

            // add the point graphic to the graphics overlay
            graphicsOverlay.getGraphics().add(pointGraphic);
        }
    }

    /**
     * method adds a polyline connecting all the airports in the list
     *
     * @param airports, list of airports
     */
    private void addPolyLine(List<IAirport> airports) {

        // create a point collection with a spatial reference
        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        //iterate through airport array and add the points to it
        for (int i = 0; i < airports.size(); i++) {
            String latLong = airports.get(i).getLatitude() + "* | " + airports.get(i).getLongitude() + "*";
            polylinePoints.add(CoordinateFormatter.fromLatitudeLongitude(latLong, SpatialReferences.getWgs84()));
        }

        // create a polyline geometry from the point collection
        Polyline polyline = new Polyline(polylinePoints);

        // create a blue line symbol for the polyline
        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF0063FF, 3);

        // create a polyline graphic with the polyline geometry and symbol
        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);

        // add the polyline graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(polylineGraphic);
    }

    /**
     * method creates the mini stage for user input
     */
    private void retrieveDataFromUser() {
        //create a new stage called flightDataScreen
        Stage flightDataScreen = new Stage();

        //set its modality
        flightDataScreen.initModality(Modality.APPLICATION_MODAL);

        //create stackpane and scene and set background
        StackPane stackPane = new StackPane();
        RadialGradient shadePaint = new RadialGradient(
                0, 2, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(1, Color.SEASHELL),
                new Stop(0, Color.LIGHTSEAGREEN));
        stackPane.setBackground(new Background(new BackgroundFill(shadePaint, null, new Insets(10))));
        Scene scene = new Scene(stackPane);

        //set the scene for the new stage
        flightDataScreen.setScene(scene);

        //set the size of the stage (popup size)
        flightDataScreen.setWidth(600);
        flightDataScreen.setHeight(550);

        //disable the x, minimize and full screen buttons
        flightDataScreen.initStyle(StageStyle.UNDECORATED);

        //show the stage
        flightDataScreen.show();

        //create vbox and add it to the stackpane
        VBox popUpVBox = new VBox();
        popUpVBox.setSpacing(5);
        stackPane.getChildren().addAll(popUpVBox);
        popUpVBox.setAlignment(Pos.CENTER);

        //create a title label for popup and add to vbox
        Label popUpTitle = new Label("Shortest Trip Planner");
        popUpTitle.setStyle("-fx-font-size: 45");
        popUpVBox.getChildren().add(popUpTitle);

        //create a label for popup and add to vbox
        Label popUpInstr = new Label("Please fill out the following fields:");
        popUpInstr.setStyle("-fx-font-size: 20");
        popUpVBox.getChildren().add(popUpInstr);

        //Create combo boxes using AutoCompleteComboBoxListener for the inputs and add them to vbox
        ComboBox<String> source = new ComboBox();
        AutoCompleteComboBoxListener sourceAuto = new AutoCompleteComboBoxListener(source, airportGraph);
        source.setMaxWidth(200);
        source.setEditable(true);
        source.setValue("Departure City");
        ComboBox<String> destination = new ComboBox();
        AutoCompleteComboBoxListener destinationAuto = new AutoCompleteComboBoxListener(destination, airportGraph);
        destination.setMaxWidth(200);
        destination.setEditable(true);
        destination.setValue("Arrival City");

        //add the airports to the source and destination combo boxes as autocomplete options
        for(IAirport a : allAirports){
            source.getItems().add(a.toTrieString());
            destination.getItems().add(a.toTrieString());
            allAirportsTrieString.add(a.toTrieString());
        }

        //add to source and destination to vBox
        popUpVBox.getChildren().add(source);
        popUpVBox.getChildren().add(destination);

        //create a label for error warnings and add to vbox
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-font-size: 20");
        errorLabel.setTextFill(Color.RED);
        popUpVBox.getChildren().add(errorLabel);

        //create button to find the trip, and add to vbox
        Button findTrip = new Button("Find my shortest trip!");
        popUpVBox.getChildren().add(findTrip);

        //create a quit button and add to vbox
        Button quitButton = new Button("Quit Program");
        popUpVBox.getChildren().add(quitButton);

        //when quit button is clicked, call the Platform.exit() method
        quitButton.setOnAction(event -> {
            Platform.exit();
        });

        //when findTrip button is clicked
        findTrip.setOnAction(event -> {

            //get the airports for start and end
            String startStr = source.getValue();
            String endStr = destination.getValue();

            // if the airports are not contained in the arraylist of airports, set error label text
            if((allAirportsTrieString.contains(startStr) == false) || (allAirportsTrieString.contains(endStr) == false)){
                errorLabel.setText("Please select values from the drop downs.");
            }
            else {
                //reset error label
                errorLabel.setText("");

                //get start and end airports
                IAirport start = airportHashtable.get(startStr);
                IAirport end = airportHashtable.get(endStr);


                //reload the optimized graph
                this.airportGraph = new AirportGraph();
                this.loadOptimizedGraph(start, end);

                //hide the stage
                flightDataScreen.hide();

                //call the calculateTrip method using the start and end airports
                calculateTrip(start, end);
            }
        });
    }

    /**
     * method calculates the shortest trip, shows a new stage with information about the trip, adds points and polyline to graph, and adds popup to main stage
     *
     * @param start, airport to start at
     * @param end,   airport to end at
     */
    private void calculateTrip(IAirport start, IAirport end) {

        //find the shortest path
        List<IAirport> shortestPath = airportGraph.shortestPath(start, end);

        //create a string to hold the details about the shortest trip
        String shortestPathString = shortestRouteToString(shortestPath);

        //add the points and lines for the path
        this.addPoints(shortestPath);
        this.addPolyLine(shortestPath);

        //set mapView viewpoint
        mapView.setViewpoint(new Viewpoint(shortestPath.get(0).getLatitude(), shortestPath.get(0).getLongitude(), 144447.638572));

        Stage calculateTrip = new Stage();
        calculateTrip.initModality(Modality.APPLICATION_MODAL);

        //create stackpane and scene and set background
        StackPane stackPane = new StackPane();
        RadialGradient shadePaint = new RadialGradient(
                0, 2, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(1, Color.SEASHELL),
                new Stop(0, Color.LIGHTSEAGREEN));
        stackPane.setBackground(new Background(new BackgroundFill(shadePaint, null, new Insets(10))));
        Scene scene = new Scene(stackPane);
        //set the scene for the popup
        calculateTrip.setScene(scene);

        //set the size of the stage (popup size)
        calculateTrip.setWidth(600);
        calculateTrip.setHeight(550);

        //disable the x, minimize and full screen buttons
        calculateTrip.initStyle(StageStyle.UNDECORATED);

        //show the stage
        calculateTrip.show();

        //create vbox and add it to the stackpane
        VBox resultsVBox = new VBox();
        resultsVBox.setSpacing(5);
        stackPane.getChildren().addAll(resultsVBox);
        resultsVBox.setAlignment(Pos.CENTER);

        //create a title label for popup and add to vbox
        Label resultsTitle = new Label("Your Shortest Trip:");
        resultsTitle.setStyle("-fx-font-size: 45");
        resultsVBox.getChildren().add(resultsTitle);

        //create a label for popup and add to vbox
        Label resultsDetailsLabel = new Label(shortestPathString);
        resultsDetailsLabel.setStyle("-fx-font-size: 15");
        resultsDetailsLabel.setMaxWidth(500);
        resultsDetailsLabel.setWrapText(true);
        resultsDetailsLabel.setAlignment(Pos.CENTER);
        resultsDetailsLabel.setTextAlignment(TextAlignment.CENTER);
        resultsVBox.getChildren().add(resultsDetailsLabel);

        //create a labels for popup and add to vbox
        Label minimizeLabel = new Label("Click the minimize button to close this screen.");
        Label detailsLabel = new Label("Use the details button to bring back this screen.");
        Label tripLabel = new Label("Use the find a new trip button to enter new trip details.");
        resultsVBox.getChildren().add(minimizeLabel);
        resultsVBox.getChildren().add(detailsLabel);
        resultsVBox.getChildren().add(tripLabel);

        //create a button to minimize trip details
        Button minimize = new Button("Minimize");
        resultsVBox.getChildren().add(minimize);

        minimize.setOnAction(event -> {
            //hide the stage
            calculateTrip.hide();
        });

        //create a quit button and add to vbox
        Button quitButton = new Button("Quit Program");
        resultsVBox.getChildren().add(quitButton);

        //when quit button is clicked, call the Platform.exit() method
        quitButton.setOnAction(event -> {
            Platform.exit();
        });

        //create popup and vbox, add vbox to popup
        newSearchButtonPopUp = new Popup();
        VBox vbox = new VBox();
        newSearchButtonPopUp.getContent().add(vbox);

        //create a button to enter new trip details
        Button newSearch = new Button("Find a new trip");
        newSearchButtonPopUp.setX(70);
        newSearchButtonPopUp.setY(110);
        newSearch.setMinWidth(100);
        newSearch.setMinHeight(40);
        vbox.getChildren().add(newSearch);

        //create a button to get existing trip details
        Button detailsButton = new Button("Trip Details");
        detailsButton.setMinWidth(100);
        detailsButton.setMinHeight(40);
        vbox.getChildren().add(detailsButton);
        newSearchButtonPopUp.show(mainStage);

        newSearchButtonPopUp.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);

        //when new search button is clicked
        newSearch.setOnAction(event2 -> {
            //reset graphics overlay
            graphicsOverlay.getGraphics().clear();

            //call the retrieveDataFromUser() method
            retrieveDataFromUser();

            //hide the calculate trip stage and the popup buttons
            calculateTrip.hide();
            newSearchButtonPopUp.hide();
        });

        // when details button is clicked
        detailsButton.setOnAction(event3 -> {
            //show the stage
            calculateTrip.show();
        });
    }

    /**
     * method loads the airportGraph
     */
    private void loadOptimizedGraph(IAirport start, IAirport end) {
        airportGraph = new AirportGraph(); // Graph which is going to hold all airports and their respective routes
        airportGraph.loadGraph(start, end, allAirports, airportLoader); // Loading all airports into the graph
    }

    private String shortestRouteToString(List<IAirport> shortestPath){
        //create string to return
        String shortestRouteToString = "\n\n";

        //format string
        for(int i = 0; i < shortestPath.size() - 1; i++){
            int index = i+1;
            shortestRouteToString = shortestRouteToString + index + ": " + shortestPath.get(i).toTrieString() + "\n\n";
        }
        shortestRouteToString = shortestRouteToString + shortestPath.size() + ": " +shortestPath.get(shortestPath.size() - 1) + ". \n\n\n\n";

        //returns string
        return shortestRouteToString;
    }

    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

}
