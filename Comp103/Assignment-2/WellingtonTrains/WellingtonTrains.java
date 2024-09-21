// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 2
 * Name: David Pranata Timothy Nangoi
 * Username: nangoidavi
 * ID: 300604132
 */

import ecs100.*;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.nio.file.*;

/**
 * WellingtonTrains
 * A program to answer queries about Wellington train lines and timetables for
 *  the train services on those train lines.
 *
 * See the assignment page for a description of the program and what you have to do.
 */

public class WellingtonTrains{
    //Fields to store the collections of Stations and Lines
    /*# YOUR CODE HERE */
    private Map<String, Station> stations = new HashMap<>();
    private Map<String, TrainLine> trainLines = new HashMap<>();

    // Fields for the suggested GUI.
    private String stationName;        // station to get info about, or to start journey from
    private String lineName;           // train line to get info about.
    private String destinationName;
    private int startTime = 0;         // time for enquiring about

    private static boolean loadedData = false;  // used to ensure that the program is called from main.

    /**
     * main method:  load the data and set up the user interface
     */
    public static void main(String[] args){
        WellingtonTrains wel = new WellingtonTrains();
        wel.loadData();   // load all the data
        wel.setupGUI();   // set up the interface
    }

    /**
     * Load data files
     */
    public void loadData(){
        loadStationData();
        UI.println("Loaded Stations");
        loadTrainLineData();
        UI.println("Loaded Train Lines");
        // The following is only needed for the Completion and Challenge
        loadTrainServicesData();
        UI.println("Loaded Train Services");
        loadedData = true;
    }

    /**
     * User interface has buttons for the queries and text fields to enter stations and train line
     * You will need to implement the methods here.
     */
    public void setupGUI(){
        UI.setDivider(1.5);
        UI.setFontSize(0.3);
        UI.setWindowSize(1920,1080);

        UI.addButton("All Stations",        this::listAllStations);
        UI.addButton("Stations by name",    this::listStationsByName);
        UI.addButton("All Lines",           this::listAllTrainLines);
        UI.addTextField("Station",          (String name) -> {this.stationName=name;});
        UI.addTextField("Train Line",       (String name) -> {this.lineName=name;});
        UI.addTextField("Destination",      (String name) -> {this.destinationName=name;});
        UI.addTextField("Time (24hr)",      (String time) ->
            {try{this.startTime=Integer.parseInt(time);}catch(Exception e){UI.println("Enter four digits");}});
        UI.addButton("Lines of Station",    () -> {listLinesOfStation(this.stationName);});
        UI.addButton("Stations on Line",    () -> {listStationsOnLine(this.lineName);});
        //UI.addButton("Stations connected?", () -> {checkConnected(this.stationName, this.destinationName);});
        //UI.addButton("Next Services",       () -> {findNextServices(this.stationName, this.startTime);});
        //UI.addButton("Find Trip",           () -> {findTrip(this.stationName, this.destinationName, this.startTime);});

        UI.addButton("Quit", UI::quit);
        UI.setMouseListener(this::doMouse);

        UI.setWindowSize(900, 400);
        UI.setDivider(0.2);
        // this is just to remind you to start the program using main!
        if (! loadedData){
            UI.setFontSize(36);
            UI.drawString("Start the program from main", 2, 36);
            UI.drawString("in order to load the data", 2, 80);
            UI.sleep(2000);
            UI.quit();
        }
        else {
            UI.drawImage("data/geographic-map.png", 0, 0);
            UI.drawString("Click to list closest stations", 2, 12);
        }
    }

    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            /*# YOUR CODE HERE */

        }
    }

    // Methods for loading data and answering queries

    /*# YOUR CODE HERE */

    public void loadStationData(){
        try {
            Scanner scanner = new Scanner(new File("data/stations.data"));
            while (scanner.hasNext()) {
                String name = scanner.next();
                int fareZone = scanner.nextInt();
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                Station station = new Station(name, fareZone, x, y);
                stations.put(name, station);
            }
            scanner.close();
            UI.println("Loaded Stations");
        } catch (FileNotFoundException e) {
            UI.println("Error loading station data: " + e.getMessage());
        }
    }

    void loadTrainLineData(){
        try {
            Scanner sc = new Scanner(new File("data/train-lines.data"));
            while (sc.hasNext()) {
                String lineName = sc.next();
                TrainLine line = new TrainLine(lineName);
                Scanner sc2 = new Scanner(new File("data/" + lineName + "-stations.data"));
                while (sc2.hasNext()) {
                    String stationName = sc2.next();
                    Station station = stations.get(stationName);
                    line.addStation(station);
                    station.addTrainLine(line);
                }
                sc2.close();
                trainLines.put(lineName, line);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            UI.println("Error loading train line data: " + e.getMessage());
        }
    }

    public void loadTrainServicesData(){
    //completion
    }

    public void listAllStations(){
        UI.println("Here is the list of all stations: ");

        for (Station stations : stations.values()){
            UI.println(stations.getName());
        }
    }

    public void listStationsByName(){
        UI.println("Here is the list of all stations in alphabetical order: ");

        // Create a list to store the Station objects
        List<Station> stationList = new ArrayList<>(stations.values());

        // Sort the list of Station objects based on their names
        // You can use Collections.sort() with a custom comparator
        Collections.sort(stationList, new Comparator<Station>() {
            @Override //I put it there in case there is typo in the method name
            public int compare(Station s1, Station s2) {
                return s1.getName().compareTo(s2.getName());
            }
        });

        // Print the names of the stations in alphabetical order
        for (Station station : stationList) {
            UI.println(station.getName());
        }
    }

    public void listAllTrainLines(){
    for (TrainLine line : trainLines.values()){
        UI.println(line);
        }
    }

    public void listLinesOfStation(String stationName) {
        // Find the station with the given name in the stations HashMap
        Station station = stations.get(stationName);

        if (station == null) {
            UI.println("Station not found.");
        } else {
            // Get the train lines that go through the station
            Set<TrainLine> trainLinesOfStation = station.getTrainLines();

            // Print the names of the train lines
            if (trainLinesOfStation.isEmpty()) {
                UI.println("No train lines go through this station.");
            } else {
                UI.println("Train lines that go through " + stationName + ":");
                for (TrainLine line : trainLinesOfStation) {
                    UI.println(line.getName());
                }
            }
        }
    }

    public void listStationsOnLine(String lineName) {
        // Find the train line with the given name in the trainLines HashMap
        TrainLine trainLine = trainLines.get(lineName);

        if (trainLine == null) {
            UI.println("Train line not found.");
        } else {
            // Get the list of stations on the train line
            List<Station> stationsOnLine = trainLine.getStations();

            // Print the names of the stations on the train line
            UI.println("Stations on the " + lineName + " train line:");
            for (Station station : stationsOnLine) {
                UI.println(station.getName());
            }
        }
    }

    public void printTrainLineFromTo(String stationName, String destinationName) {
        // Retrieve the Station objects for the start and destination stations
        Station startStation = stations.get(stationName);
        Station destinationStation = stations.get(destinationName);

        if (startStation == null || destinationStation == null) {
            UI.println("Invalid station name(s).");
        } else {
            // Check if there is a valid train line between the two stations
            for (TrainLine trainLine : startStation.getTrainLines()) {
                List<Station> stationsOnLine = trainLine.getStations();
                int startIndex = stationsOnLine.indexOf(startStation);
                int destinationIndex = stationsOnLine.indexOf(destinationStation);

                if (startIndex != -1 && destinationIndex != -1 && startIndex < destinationIndex) {
                    UI.println("The train line from " + stationName + " to " + destinationName + " is: " + trainLine.getName());
                    return;
                }
            }

            // If no valid train line is found
            UI.println("There is no valid train line from " + stationName + " to " + destinationName + ".");
        }
    }


}
