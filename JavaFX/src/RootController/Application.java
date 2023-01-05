package RootController;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class Application extends javafx.application.Application {
    RootController rootController;
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Enigma Project");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                           @Override
                                           public void handle(WindowEvent event) {
                                            /*   if (rootController.getBruteForceComponentController().getDm() != null) {
                                                   rootController.getBruteForceComponentController().getDm().killAllThreadsWhileExit();
                                                   exit(0);
                                               }*/
                                           }
                                       });
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("sagiEnigma.fxml");
        fxmlLoader.setLocation(url);
        Parent load =  fxmlLoader.load();
        rootController =fxmlLoader.getController();
       // rootController.setMachineTabImageScales(primaryStage);
        Scene scene = new Scene(load,895,800);
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getClassLoader().getResource("Resources/logo.jpg"))));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
