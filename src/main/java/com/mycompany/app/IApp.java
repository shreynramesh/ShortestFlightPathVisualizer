// --== CS400 File Header Information ==--
// Name: Vishesh Dhawan
// Email: vdhawan2@wisc.edu
// Team: AL Blue
// TA: Yelun Bao
// Lecturer: Dahl
// Notes to Grader: I have used the ArcGIS API and their documentation in this project for the map as well as map based elements.
// There is also no tester since the TA's at office hours said that there was no way for me to test my code, since the JavaFXTester
// would not work, and the only other tests I could do would be integration tests.

package com.mycompany.app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * interface for app class
 */
public interface IApp {

    /**
     * start method for starting the program
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    void start(Stage stage);

    /**
     * Stops and releases all resources used in application.
     */
    void stop();
}
