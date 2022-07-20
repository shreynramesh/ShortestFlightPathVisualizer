package com.mycompany.app;
// --== CS400 Project Two File Header ==--
// Name: Rishi Mandyam
// CSL Username: mandyam
// Email: rmandyam@wisc.edu
// Lecture #: 001 @11:00am

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rishi Mandyam
 */
public class AirportLoader implements IAirportLoader {

  private ArrayList<IAirport> airportsFromFile;

  /**
   * Constructor
   */
  public AirportLoader() {
    airportsFromFile = new ArrayList<>();
  }

  /**
   * Generates list of airports without routes from the JSON data file.
   *
   * @param filePath filepath of datafile
   * @return a list of Airports
   * @throws IOException           for file errors
   * @throws FileNotFoundException for file errors
   * @throws ParseException        for file errors
   */
  @Override
  public ArrayList<IAirport> loadFlights(String filePath) throws IOException, FileNotFoundException, ParseException {
    JSONParser parser = new JSONParser();

    try (FileReader reader = new FileReader(filePath)) {
      // Read json file
      Object obj = parser.parse(reader);
      JSONArray jsonList = (JSONArray) obj;
      // ArrayList<IAirport> airports = new ArrayList<>();

      for (int i = 0; i < jsonList.size(); i++) {
        JSONObject jsonObject = (JSONObject) jsonList.get(i);
        String airportName = (String) jsonObject.get("Airport Name");
        String airportCity = (String) jsonObject.get("City");
        String airportCountry = (String) jsonObject.get("Country");
        double airportLatitude = Double.parseDouble(jsonObject.get("Latitude").toString());
        double airportLongitude = Double.parseDouble(jsonObject.get("Longitude").toString());

        IAirport airport = new Airport(airportName, airportCity, airportCountry, airportLatitude,
            airportLongitude); // makes the airport object
        airportsFromFile.add(airport); // adds the airport object to the ArrayList of Airports
      }
      return airportsFromFile;
    } catch (FileNotFoundException f) {
      f.printStackTrace();
    } catch (ParseException p) {
      p.printStackTrace();
    }

    catch (IOException i) {
      i.printStackTrace();
    }
    return null;
  }

  @Override
  public ArrayList<String> getAirportNames() throws NullPointerException, IOException, ParseException {
    ArrayList<IAirport> airports = loadFlights("filepath");
    ArrayList<String> airportNames = new ArrayList<>();
    for (int i = 0; i < airports.size(); i++) {
      String name = airports.get(i).getName();
      airportNames.add(name);
    }
    return airportNames;
  }

  /**
   * This method loads routes into the source, destination, and all possible
   * airports in between.
   * If the source airport is an international airport, then it routes are all
   * other international airports in the destination country.
   * If the source airport is a domestic airport, then it routes to all
   * international airports in its country and those international airports route
   * to all internation airports in the destination country.
   * Then, then all airports in the destination country route to the destination
   * airport if they themselves are not the destination.
   * 
   * @param source
   * @param destination
   */
  public void loadRoutes(IAirport source, IAirport destination) {

    if (source.equals(destination)) {
      return;
    }

    if (source.getCountry().equals(destination.getCountry()) && source.isInternational()
        && destination.isInternational()) { // If both airports are international airports that are in the same country
                                            // than there will be a direct flight between them.
      source.setRoute(destination);
      return;
    }
    List<IAirport> sourceInternationals = findInternationalAirports(source); // Finding a list of international airports
                                                                             // in the source country

    List<IAirport> destinationInternationals = findInternationalAirports(destination); // Finding a list of
                                                                                       // international airports in the
                                                                                       // destination country
    destinationInternationals = destinationInternationals.stream().filter(a -> !a.equals(source))
        .collect(Collectors.toList());
    if (source.isInternational()) { // Checks whether the source airport is an international airport

      if (destinationInternationals.isEmpty()) { // This means that there are are no international airports in the
                                                 // country and we will offer a direct flight to the destination
        source.setRoute(destination);
        return; // No more routes to be added
      } else {
        source.setRoutes(new ArrayList<IAirport>(destinationInternationals)); // Setting source routes to be all
                                                                              // international airports in the
                                                                              // destination country.
        for (IAirport a : destinationInternationals) {
          a.setRoute(destination); // Adding the destination as a route to all international airports in the
                                   // destination country
        }

        return; // No more routes to be added
      }
    } else { // The source airport is not an international airport so we need to route it to
             // the international airports in its own country. If there are no international
             // airports in its own country then we route it the international airports in
             // the destnation country;

      if (sourceInternationals.isEmpty()) {
        source.setRoutes(new ArrayList<IAirport>(destinationInternationals));

        for (IAirport a : destinationInternationals) {
          a.setRoute(destination); // Adding the destination as a route to all international airports in the
                                   // destination country
        }
        return; // No more routes to be added
      } else {
        source.setRoutes(new ArrayList<IAirport>(sourceInternationals)); // Adding international airports in the source
                                                                         // country as routes from the source.
        for (IAirport a1 : sourceInternationals) {
          a1.setRoutes(new ArrayList<IAirport>(destinationInternationals)); // Adding international airports in the
                                                                            // destination country as routes from the
                                                                            // international airports in the source.
        }

        for (IAirport a : destinationInternationals) {
          a.setRoute(destination); // Adding the destination as a route to all international airports in the
                                   // destination country
        }
        return; // No more routes to be added
      }
    }
  }

  private List<IAirport> findInternationalAirports(IAirport airport) {
    List<IAirport> result = new ArrayList<IAirport>();
    String destinationCountry = airport.getCountry();

    result = airportsFromFile.stream()
        .filter(a -> !a.equals(airport) && a.getCountry().equals(destinationCountry) && a.isInternational())
        .collect(Collectors.toList()); // Filters all the airports by their country and whether they are an
                                       // international airport

    return result;
  }
}
