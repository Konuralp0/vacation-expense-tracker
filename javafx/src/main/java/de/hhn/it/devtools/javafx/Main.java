package de.hhn.it.devtools.javafx;

import de.hhn.it.devtools.javafx.controllers.Controller;
import de.hhn.it.devtools.javafx.controllers.RootController;
import de.hhn.it.devtools.javafx.modules.Module;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * The type Main.
 */
public class Main extends Application {

  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(Main.class);
  private final int Width = 1280;

  private final int Height = 720;
  private RootController rootController;
  private final Map<String, Module> moduleMap;

  /**
   * Instantiates a new Main.
   */
  public Main() {
    moduleMap = new HashMap<>();
  }

  /**
   * the main method.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    System.out.println("java version: " + System.getProperty("java.version"));
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Root2.fxml"));

    final Parent root = loader.load();
    rootController = loader.getController();

    primaryStage.setTitle("JavaFX UI");
    Scene scene = new Scene(root, Width, Height);
    primaryStage.setMinWidth(Width);
    primaryStage.setMinHeight(Height);
    primaryStage.setScene(scene);
    primaryStage.show();


    addModule("VacTrackService");

  }

  @Override
  public void stop() {
    logger.info("stop: Shutting down");
    // This is automatically called then you terminate the application using the window controls
    // ("x it out", "quit it", ...). It does not get called when you terminate the application
    // using control-C or an OS command like "kill".

    rootController.shutdown();
  }

  private void addModule(String name) {
    try {
      logger.info("addModule: Loading Module: \"" + name + "\"!");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + name + ".fxml"));
      Node content = loader.load();
      Controller controller = loader.getController();
      Module module = new Module(name, controller, content);
      rootController.addModule(module);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(8);
    }
  }
}
